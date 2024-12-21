<template>
  <a-button type="outline" @click="handleClick">AI 生成题目</a-button>
  <a-drawer :width="340" :visible="visible" @ok="handleOk" @cancel="handleCancel" :footer="false" unmountOnClose>
    <template #title>
      AI 生成题目
    </template>
    <div>
      <a-form
          :model="form"
          label-align="left"
          auto-label-width
          @submit="handleSubmit"
      >
        <a-form-item field="questionNumber" label="应用id">
          {{ props.appId }}
        </a-form-item>
        <a-form-item field="questionNumber" label="题目数量">
          <a-input-number v-model="form.questionNumber" min="0" max="20" placeholder="请输入题目数量"/>
        </a-form-item>
        <a-form-item field="optionNumber" label="选项数量">
          <a-input-number v-model="form.optionNumber" min="2" max="6" placeholder="请输入选项数量"/>
        </a-form-item>
        <a-form-item>
          <a-space size="medium">
            <a-button :loading="submitting" type="primary" html-type="submit" style="width: 120px">
              {{ submitting ? "生成中" : "一键生成" }}
            </a-button>
            <a-button :loading="submitting" style="width: 120px" @click="handleSseSubmit">
              {{ submitting ? "生成中" : "实时生成" }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {aiGenerateQuestionUsingPost} from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";


interface Props {
  appId: string;
  onSuccess?: (data: API.QuestionContentDTO[]) => void;
  onSseSuccess?: (data: API.QuestionContentDTO) => void;
  onSseStart?: (event) => void;
  onSseClose?: (event) => void;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const form = ref({
  optionNumber: 2,
  questionNumber: 10
}) as API.AiGenerateQuestionRequest


const visible = ref(false);
const submitting = ref(false);

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};
const handleSubmit = async () => {
  let res: any;
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  res = await aiGenerateQuestionUsingPost({
    appId: props.appId,
    ...form.value
  });
  console.log(res)
  if (res.data.code === 0 && res.data.data.length > 0) {
    if (props.onSuccess) {
      props.onSuccess(res.data.data)
    }
    visible.value = false;
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;
}
/**
 * 提交（实时生成）
 */
const handleSseSubmit = async () => {
  let res: any;
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  // 创建Sse请求
  const eventSource = new EventSource(
      // todo 手动填写完整的后端地址
      "http://localhost:8101/api/question/ai_generate/sse" +
      `?appId=${props.appId}&optionNumber=${form.value.optionNumber}&questionNumber=${form.value.questionNumber}`
  )
  eventSource.onmessage = (event) => {
    console.log(event.data)
    props.onSseSuccess?.(JSON.parse(event.data));
  }
  eventSource.onerror = (event) => {
    if (event.eventPhase === EventSource.CLOSED) {
      console.log("关闭连接")
      eventSource.close()
      props.onSseClose?.(event);
      submitting.value = false;
    }
  }
  eventSource.onopen = (event) => {
    console.log("建立连接")
    props.onSseStart?.(event);
    handleCancel();
  }
}

</script>
