<template>
  <a-layout-footer style="text-align: center">
    MuMu Wiki ©2021 Created by MuMu <span v-if="user.id"> - 欢迎您：{{ user.name }}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent} from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user)

    return {
      user
    }
  }
});
</script>