<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline" :model="param"
        >
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleSearchByName()">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">
              新增
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery">
              显示所有
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categoryTree"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确定要删除？"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option value="0">
            无
          </a-select-option>

          <a-select-option v-for="c in categoryTree" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '排序',
        key: 'sort',
        dataIndex: 'sort'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    // -------- 表单 ---------
    const categoryTree = ref();
    const category = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      //向后台请求保存分类
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;

        const data = response.data;
        if (data.success) {
          modalVisible.value = false;
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 通过分类查询
     **/
    const handleSearchByName = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      categorys.value = [];
      axios.get("/category/search", {
        params: {
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          handleQuery();
        }
      });
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    /**
     * 查询所有
     **/
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      categorys.value = [];
      param.value = {};
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
          categoryTree.value = [];
          categoryTree.value = Tool.array2Tree(categorys.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      categoryTree,
      category,
      columns,
      loading,
      param,

      handleModalOk,
      handleQuery,

      edit,
      add,
      handleDelete,
      handleSearchByName,

      modalVisible,
      modalLoading
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>