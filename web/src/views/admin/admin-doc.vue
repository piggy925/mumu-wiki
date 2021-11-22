<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
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
            </a-form>
          </p>

          <a-table
              v-if="docTree.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="docTree"
              :pagination="false"
              :loading="loading"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" size="small" @click="edit(record)">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确定要删除？"
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button size="small" type="danger">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
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
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent">
                <EyeOutlined/>
                内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer width="900" placement="right" :closable="false" :visible="drawVisible" @close="onDrawerClose">
        <div class="wangEditor" :innerHTML="previewHTML"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor';

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    const docTreeSelectData = ref();
    docTreeSelectData.value = [];

    const columns = [
      {
        title: '名称',
        key: 'name',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    // -------- 富文本预览 ---------
    const drawVisible = ref(false);
    const previewHTML = ref();
    const handlePreviewContent = () => {
      previewHTML.value = editor.txt.html();
      drawVisible.value = true;
    };
    const onDrawerClose = () => {
      drawVisible.value = false;
    };

    // -------- 表单 ---------
    const editor = new E('#content')
    editor.config.zIndex = 0;
    const docTree = ref();
    docTree.value = [];
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const handleSave = () => {
      doc.value.content = editor.txt.html();
      //向后台请求保存文档
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("保存成功")
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
      //清空富文本编辑器中的内容
      editor.txt.html("");
      doc.value = {
        ebookId: route.query.ebookId
      };

      docTreeSelectData.value = Tool.copy(docTree.value) || [];
      //为树形选择增加'无'选项，用于新增一级节点
      docTreeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 获取文档内容
     **/
    const getContent = () => {
      axios.get("/doc/get-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      //清空富文本编辑器中的内容
      editor.txt.html("");
      doc.value = Tool.copy(record);
      getContent();
      docTreeSelectData.value = Tool.copy(docTree.value) || [];
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
      axios.get("/doc/all/" + doc.value.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          docTree.value = [];
          docTree.value = Tool.array2Tree(docs.value, 0);

          docTreeSelectData.value = Tool.copy(docTree.value) || [];
          //为树形选择增加'无'选项，用于新增一级节点
          docTreeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
      editor.create();
    });

    return {
      docTreeSelectData,
      docTree,
      doc,
      columns,
      loading,
      param,
      drawVisible,
      previewHTML,

      handleSave,
      handleQuery,

      edit,
      add,
      handleDelete,
      handleSearchByName,
      handlePreviewContent,
      onDrawerClose
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