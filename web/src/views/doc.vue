<template>
  <a-layout>
    <a-layout-content
        :style="{
        background: '#fff',
        padding: '24px',
        margin: 0,
        minHeight: '280px',
      }"
    >
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="docTree.length > 0"
              :tree-data="docTree"
              @select="onSelect"
              :replaceFields="{ title: 'name', key: 'id', value: 'id' }"
              :defaultExpandAll="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div class="wangEditor" :innerHTML="html"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const docTree = ref();
    docTree.value = [];
    const html = ref();

    /**
     * 获取文档内容
     **/
    const getContent = (id: number) => {
      axios.get("/doc/get-content/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          html.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectKeys: any) => {
      if (Tool.isNotEmpty(selectKeys)) {
        getContent(selectKeys[0]);
      }
    };

    /**
     * 查询所有
     **/
    const handleQuery = () => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      docTree.value = [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
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
      docTree,
      html,

      onSelect,
      handleQuery
    }
  }
});
</script>

<style>
/* table 样式 */
.wangEditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangEditor table td,
.wangEditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangEditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangEditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangEditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangEditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangEditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 解决与antd样式冲突问题 */
.wangEditor p {
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 500;
}
</style>