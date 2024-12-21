<template>
  <a-row align="center" :wrap="false" id="globalHeader" style="margin-bottom: 16px;">
    <a-col flex="auto">
      <a-menu mode="horizontal"
              :selected-keys="selectedKeys"
              @menu-item-click="doMenuClick"
      >
        <a-menu-item key="0" :style="{ padding: 0, marginRight: '38px' }" disabled>
          <div class="title-bar">
            <img class="logo" src="../assets/logo.png" alt="logo"/>
            <div class="title">融答答</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visiableRoutes" :key="item.path">{{ item.name }}</a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div v-if="loginUserStore.loginUser.id">
        {{ loginUserStore.loginUser.userName ?? '无名' }}
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup name="GlobalHeader">
import {routes} from '@/router/routes'
import {useRouter} from "vue-router";
import {computed, ref} from "vue";
import {useLoginUserStore} from "@/stores/userStore.ts";
import checkAccess from "@/access/checkAccess.ts";

const router = useRouter();
const loginUserStore = useLoginUserStore()
//过滤菜单menu
const visiableRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false
    }
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access)) {
      return false
    }
    return true
  })
})
// 当前选中的菜单项
const selectedKeys = ref(['/'])
//路由跳转时，自动更新选中的菜单项
router.afterEach((to) => {
  selectedKeys.value = [to.path];
})
// 点击菜单，跳转对应页面
const doMenuClick = (key) => {
  router.push({
    path: key,
  })
}
</script>

<style scoped lang="scss">
#globalHeader {

  box-shadow: #eee 1px 1px 5px;

  .title-bar {
    display: flex;
    align-items: center;

    .logo {
      height: 48px;
    }

    .title {
      margin-left: 10px;
    }
  }

  .menu-demo {
    box-sizing: border-box;
    width: 100%;
    padding: 40px;
    background-color: var(--color-neutral-2);
  }
}
</style>