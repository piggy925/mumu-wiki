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
            <a-button type="primary" @click="handleSearchByName({pageNum: 1, pageSize: pagination.pageSize})">
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
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>

        <template v-slot:category="{text, record}">
          <span>
            {{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}
          </span>
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
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>

      <a-form-item label="分类">
        <a-cascader v-model:value="categoryIds"
                    :field-names="{label: 'name', value: 'id', children: 'children'}"
                    :options="categoryTree" placeholder="请选择"/>
      </a-form-item>

      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea"/>
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
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    // -------- 表单 ---------
    const categoryTree = ref();
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      //向后台请求保存图书
      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;

        const data = response.data;
        if (data.success) {
          modalVisible.value = false;

          handleQuery({
            pageNum: pagination.value.current,
            pageSize: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 通过书名查询
     **/
    const handleSearchByName = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      axios.get("/ebook/search", {
        params: {
          pageNum: params.pageNum,
          pageSize: params.pageSize,
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.pageNum;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          handleQuery({
            pageNum: pagination.value.current,
            pageSize: pagination.value.pageSize
          });
        }
      });
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          categoryTree.value = [];
          categoryTree.value = Tool.array2Tree(categorys, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 查询所有
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      param.value = {};
      axios.get("/ebook/list", {
        params: {
          pageNum: params.pageNum,
          pageSize: params.pageSize
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.pageNum;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };


    /**
     * 通过分类id获取分类名
     **/
    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          result = item.name;
        }
      });
      return result;
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      if (param.value.name == null) {
        handleQuery({
          pageNum: pagination.current,
          pageSize: pagination.pageSize
        });
      } else {
        handleSearchByName({
          pageNum: pagination.current,
          pageSize: pagination.pageSize,
          name: param.value.name
        });
      }
    };

    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize
      });
    });

    return {
      ebook,
      ebooks,
      pagination,
      columns,
      loading,
      param,
      categoryTree,
      categoryIds,

      handleTableChange,
      handleModalOk,
      handleQuery,
      handleQueryCategory,
      getCategoryName,

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