<template>
  <div id="adminUserPage">
    <a-form
        :style="{ marginBottom: '30px'  }"
        label-align="left"
        :model="searchFormParams"
        layout="inline"
        auto-label-width
        @submit="doSearch">
      <a-form-item field="userName" label="用户名">
        <a-input
            v-model="searchFormParams.userName"
            placeholder="请输入用户名"
        />
      </a-form-item>
      <a-form-item field="userProfile" label="用户简介">
        <a-input
            v-model="searchFormParams.userProfile"
            placeholder="请输入用户简介"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doReset">重置</a-button>
      </a-form-item>
    </a-form>
    <a-table
        :columns="columns"
        :data="dataList"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
        }"
        @page-change="onPageChange"
    >
      <template #userAvatar="{ record }">
        <a-image
            width="64"
            :src="record.userAvatar"
        />
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-button @click="doDelete(record)">删除</a-button>
      </template>

    </a-table>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, watchEffect} from 'vue';
import {deleteUserUsingPost, listUserByPageUsingPost} from "@/api/userController.ts";
import {Message} from "@arco-design/web-vue";
import dayjs from "dayjs";

//初始化搜索参数
const initSearchParams = {
  current: 1,
  pageSize: 10
}
//最终搜索参数
const searchParams = ref<API.UserQueryRequest>({...initSearchParams})
//表单搜索参数
const searchFormParams = ref<API.UserQueryRequest>({})
//数据源
const dataList = ref<API.User[]>([])
//表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "用户名",
    dataIndex: "userName",
  },
  {
    title: "用户头像",
    dataIndex: "userAvatar",
    slotName: "userAvatar",
  },
  {
    title: "用户简介",
    dataIndex: "userProfile",
  },
  {
    title: "权限",
    dataIndex: "userRole",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
//总记录数
let total = ref<number>(0)


//加载数据源
const loadData = async () => {
  console.log(searchParams)
  const res = await listUserByPageUsingPost(searchParams.value)
  if (res.data.code === 0) {
    // Object.assign(dataList,[...res.data.data?.records] || [])
    dataList.value = res.data.data?.records || [];
    total.value = parseInt(res.data.data?.total || 0);
  } else {
    Message.error('获取数据失败,' + res.data.message)
  }
}

//点击分页
const onPageChange = (page) => {
  searchParams.value.current = page
}

//删除用户
const doDelete = async (record: API.DeleteRequest) => {
  const res = await deleteUserUsingPost({id: record.id})
  if (res.data.code === 0) {
    Message.success('删除成功')
    await loadData()
  } else {
    Message.error('删除失败,' + res.data.message)
  }
}

//搜索
const doSearch = () => {
  searchParams.value= {...initSearchParams, ...searchFormParams.value}
}

//重置
const doReset = () => {
  searchParams.value = {...initSearchParams}
}

//初始化/监听加载数据
watchEffect(() => {
  loadData()
})

</script>
