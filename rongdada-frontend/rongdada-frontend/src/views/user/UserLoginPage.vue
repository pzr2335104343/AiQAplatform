<template>
  <div id="userLoginPage">
    <h2 style="margin-bottom: 16px">用户登录</h2>
    <a-form
        :style="{ width: '480px', margin: '0 auto' }"
        label-align="left"
        :model="form"
        @submit="handleSubmit">
      <a-form-item field="userAccount" label="账户">
        <a-input
            v-model="form.userAccount"
            placeholder="请输入账号"
        />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码不小于 8 位" label="密码">
        <a-input-password
            v-model="form.userPassword"
            placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary"  html-type="submit">登录</a-button>
        <a-button @click="router.push({path:'/user/register',replace: true})"
                  style="margin-left: 20px"
                  type="outline">
          注册
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive} from 'vue';
import {userLoginUsingPost} from "@/api/userController";
import {Message} from "@arco-design/web-vue";
import {useLoginUserStore} from "@/stores/userStore";
import {useRouter} from "vue-router";

const form = reactive({
  userAccount: '',
  userPassword: '',
} as API.UserLoginRequest);

const loginUserStore = useLoginUserStore()
const router = useRouter()

const handleSubmit = async () => {
  const res = await userLoginUsingPost(form);
  if (res.data.code === 0) {
    await loginUserStore.fetchLoginUser();
    Message.success('登录成功');
    await router.push({
      path: '/',
      replace: true
    })
  } else {
    Message.error('登录失败,' + res.data.message);
  }
};

</script>
