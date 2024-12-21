<template>
  <div id="addScoringResultPage">
    <h2 style="margin-bottom: 32px">设置评分</h2>
    <a-form
        :model="form"
        :style="{ width: '480px' }"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
    >
      <a-form-item label="应用id">
        {{ props.appId }}
      </a-form-item>
      <a-form-item v-if="updateId" label="修改评分id">
        {{ updateId }}
      </a-form-item>
      <a-form-item field="appName" label="结果名称">
        <a-input v-model="form.resultName" placeholder="请输入结果名称"/>
      </a-form-item>
      <a-form-item field="appDesc" label="结果描述">
        <a-input v-model="form.resultDesc" placeholder="请输入结果描述"/>
      </a-form-item>
      <a-form-item field="appIcon" label="结果图标">
        <a-input v-model="form.resultPicture" placeholder="请输入结果图标"/>
      </a-form-item>
      <a-form-item field="resultProp" label="结果集">
        <a-input-tag
            v-model="form.resultProp"
            :style="{ width: '320px' }"
            placeholder="请输出结果集，按回车确认"
            allow-clear
        />
      </a-form-item>
      <a-form-item field="resultScoreRange" label="结果得分范围">
        <a-input-number
            v-model="form.resultScoreRange"
            placeholder="请输入结果得分范围"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 120px">
          提交
        </a-button>
      </a-form-item>
    </a-form>
    <ScoringResultTable :appId="props.appId" :doUpdate="doUpdate" ref="tableRef"></ScoringResultTable>
  </div>
</template>

<script setup lang="ts">
import {defineProps, ref, watchEffect, withDefaults} from "vue";
import message from "@arco-design/web-vue/es/message";
import {useRouter} from "vue-router";
import {
  addScoringResultUsingPost,
  editScoringResultUsingPost,
  getScoringResultVoByIdUsingGet,
} from "@/api/scoringResultController";
import ScoringResultTable from "@/views/add/components/ScoringResultTable.vue";

interface Props {
  appId: string;
  id:number;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});
// table实体
const tableRef=ref();
// 判断是否是修改
const updateId = ref<number>();
// 路由器
const router = useRouter();
// 表单参数
const form = ref({
  resultDesc: "",
  resultPicture: "",
  resultName: "",
  resultProp: [],
  resultScoreRange: 0
} as API.ScoringResultAddRequest);

const oldScoringResult = ref<API.ScoringResultVO>();

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getScoringResultVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0 && res.data.data) {
    oldScoringResult.value = res.data.data;
    form.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

const doUpdate = function (scoringResult: API.ScoringResultVO) {
  updateId.value = scoringResult.id
  form.value = scoringResult

}

/**
 * 提交
 */
const handleSubmit = async () => {
  let res: any;
  if (!props.appId) {
    return
  }
  // 如果是修改
  if (updateId.value) {
    res = await editScoringResultUsingPost({
      id: updateId.value as any,
      ...form.value,
    });
  } else {
    // 创建
    res = await addScoringResultUsingPost({
      appId: props.appId as any,
      ...form.value
    });
  }
  if (res.data.code === 0) {
    message.success("操作成功");
  } else {
    message.error("操作失败，" + res.data.message);
  }
  if(tableRef.value){
    await tableRef.value.loadData();
  }
  await reset()
};

const reset = async () => {
  updateId.value = undefined;
  form.value = {}
  await loadData();
}
</script>