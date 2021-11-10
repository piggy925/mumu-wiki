<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in categoryTree" :key="item.id">
          <template v-slot:title>
            <UserOutlined/>
            <span>{{ item.name }}</span>
          </template>
          <a-menu-item v-for="c in item.children" :key="c.id">
            <UserOutlined/>
            <span>{{ c.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用MuMu Wiki</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" :grid="{ gutter: 16, column: 3 }" size="large"
              :pagination="pagination"
              :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {LikeOutlined, MessageOutlined, StarOutlined} from '@ant-design/icons-vue';
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import {message} from "_ant-design-vue@2.2.8@ant-design-vue";

export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    const ebooks = ref();
    const categoryTree = ref();
    const isShowWelcome = ref(true);

    const handleClick = (value: any) => {
      console.log(value);
      if (value.key === "welcome") {
        isShowWelcome.value = true;
      } else {
        isShowWelcome.value = false;
        getEbookList(value.key);
      }
    };

    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/category/all").then((response) => {
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

    const getEbookList = (id: number) => {
      axios.get('/ebook/list', {
        params: {
          pageNum: 1,
          pageSize: 1000,
          category2Id: id
        }
      }).then((resp) => {
        const data = resp.data;
        ebooks.value = data.content.list;
      })
    };

    onMounted(() => {
      handleQueryCategory();
      // 初始显示欢迎页面
      // axios.get('/ebook/all').then((resp) => {
      //   const data = resp.data;
      //   ebooks.value = data.content;
      // })
    })


    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];

    return {
      ebooks,
      actions,
      categoryTree,
      isShowWelcome,

      handleQueryCategory,
      handleClick
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>