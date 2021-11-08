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
          :data-source="docTree"
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
      title="文档表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="docTreeSelectData"
            placeholder="请选择父文档"
            tree-default-expand-all
            :replaceFields="{ title:'name', key:'id', value: 'id' }"
        >

        </a-tree-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: '父文档',
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
    const docTreeSelectData = ref();
    docTreeSelectData.value = [];
    const docTree = ref();
    const doc = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      //向后台请求保存文档
      axios.post("/doc/save", doc.value).then((response) => {
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
     * 通过名称查询
     **/
    const handleSearchByName = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      docs.value = [];
      axios.get("/doc/search", {
        params: {
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      ids = [];
      getDeleteIds(docTree.value, id);
      axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
        const data = response.data;
        if (data.success) {
          handleQuery();
        }
      });
    };

    let ids: Array<string> = [];
    /**
     * 删除文档时获取文档id与其子文档id
     **/
    const getDeleteIds = (docTreeSelectData: any, id: any) => {
      for (let i = 0; i < docTreeSelectData.length; i++) {
        const node = docTreeSelectData[i];
        if (node.id === id) {
          ids.push(node.id);
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 将指定节点与其子孙节点全部设置为disabled
     **/
    const setDisabled = (docTreeSelectData: any, id: any) => {
      for (let i = 0; i < docTreeSelectData.length; i++) {
        const node = docTreeSelectData[i];
        if (node.id === id) {
          node.disabled = true;
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisabled(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisabled(children, id);
          }
        }
      }
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      docTreeSelectData.value = Tool.copy(docTree.value);
      //为树形选择增加'无'选项，用于新增一级节点
      docTreeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      docTreeSelectData.value = Tool.copy(docTree.value);
      setDisabled(docTreeSelectData.value, record.id);
      //为树形选择增加'无'选项，用于新增一级节点
      docTreeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 查询所有
     **/
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      docTree.value = [];
      param.value = {};
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          docTree.value = [];
          docTree.value = Tool.array2Tree(docs.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      docTreeSelectData,
      docTree,
      doc,
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