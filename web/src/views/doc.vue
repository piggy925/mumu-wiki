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
          <div :innerHTML="html"></div>
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