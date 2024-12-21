<template>
  <div id="doAnswerPage">
    <a-card>
      <h1>{{ app.appName }}</h1>
      <p style="margin-top: 5px">{{ app.appDesc }}</p>
      <h2 style="margin-top: 16px">
        {{ currentQuestion?.title }}
      </h2>
      <div>
        <a-radio-group
            direction="vertical"
            v-model="currentAnswer"
            :options="currentQuestionOptions"
            @change="doRadioChange"
        />
      </div>
      <div style="margin-top: 24px">
        <a-space size="large">
          <a-button
              v-if="current < questionContent.length"
              type="primary"
              circle
              :disabled="!currentAnswer"
              @click="current += 1"
          >
            下一题
          </a-button>
          <a-button
              v-if="current===questionContent.length"
              type="primary"
              circle
              :disabled="!currentAnswer"
              @click="doSubmit"
              :loading="submitting"
          >
            {{ submitting ? "结果生成中" : "查看结果" }}
          </a-button>
          <a-button
              v-if="current > 1"
              circle
              @click="current -= 1"
              :disabled="submitting"
          >
            上一题
          </a-button>
        </a-space>
      </div>
    </a-card>

  </div>
</template>

<script setup lang="ts">
import {computed, defineProps, onMounted, reactive, ref, watchEffect, withDefaults} from "vue";
import {useRouter} from "vue-router";
import {listQuestionVoByPageUsingPost} from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";
import {getAppVoByIdUsingGet} from "@/api/appController";
import {addUserAnswerUsingPost, generateSnowAnswerIdUsingGet} from "@/api/userAnswerController";

interface Props {
  appId: string;
}

const {appId} = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});
// 是否正在提交
const submitting = ref(false);
const router = useRouter();
// 应用信息
const app = ref<API.AppVO>({})
// 题目内容结构（题目列表）
const questionContent = ref<API.QuestionContentDTO[]>([]);
// 当前题目序号
const current = ref<number>(1);
// 当前题目
const currentQuestion = ref<API.QuestionContentDTO>({})
// 当前题目选项
const currentQuestionOptions = computed(() => {
  return currentQuestion.value?.options.map((item) => {
    return {
      label: `${item.key}.${item.value}`,
      value: item.key
    }
  }) ?? [];
})
// 当前答案
const currentAnswer = ref<string>();
// 回答列表
const answerList = reactive<string[]>([]);

const id = ref<number>();
// 生成唯一id
const generateId = async ()=>{
  let res: any = await generateSnowAnswerIdUsingGet();
  if (res.data.code === 0) {
    id.value = res.data.data as any;
  } else {
    message.error("获取唯一 id 失败，" + res.data.message);
  }
}

// 进入页面时，生成唯一id
onMounted(generateId);

/**
 * 单选改变
 */
const doRadioChange = function () {
  answerList[current.value - 1] = currentAnswer.value as string;
}

/**
 * 加载数据
 */
const loadData = async () => {
  if (!appId) {
    return;
  }
  // 获取 app
  let res: any = await getAppVoByIdUsingGet({
    id: appId as any,
  });
  if (res.data.code === 0) {
    app.value = res.data.data as any;
  } else {
    message.error("获取应用失败，" + res.data.message);
  }
  // 获取题目
  res = await listQuestionVoByPageUsingPost({
    appId: appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });
  if (res.data.code === 0 && res.data.data?.records) {
    questionContent.value = res.data.data.records[0].questionContent;
  } else {
    message.error("获取题目失败，" + res.data.message);
  }
};
watchEffect(() => {
  loadData()
})

// 改变 current 题号后，会自动更新当前题目和答案
watchEffect(() => {
  currentQuestion.value = questionContent.value[current.value - 1];
  currentAnswer.value = answerList[current.value - 1];
});

/**
 * 提交
 */
const doSubmit = async () => {
  if (!appId || !answerList || answerList.length === 0) {
    return
  }
  submitting.value = true;
  let res: any;
  res = await addUserAnswerUsingPost({
    id: id.value as any,
    appId,
    choices: answerList,
  });
  if (res.data.code === 0) {
    await router.push(`/answer/result/${res.data.data}`);
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;

};
</script>