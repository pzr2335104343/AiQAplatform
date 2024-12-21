<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <UserLayout />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import UserLayout from '@/layouts/UserLayout.vue'
import {useRoute} from "vue-router";
import {onMounted} from "vue";
const route = useRoute();
const doInit= () => {
  console.log('欢迎来到我的秘密空间！')
}

onMounted(() => {
  doInit()
})

</script>


<style scoped>

</style>
