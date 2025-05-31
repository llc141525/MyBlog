<template>
  <VCard class="mb-2 rounded-lg" elevation="1">
    <VRow align="start" class="pa-4" no-gutters>
      <!-- 头像区域 -->
      <VCol class="mr-3" cols="auto">
        <VAvatar color="primary" size="48">
          <VImg
            alt="用户头像"
            :src=" 'https://cdn.vuetifyjs.com/images/john.jpg'"
          />
        </VAvatar>
      </VCol>

      <!-- 内容区域 -->
      <VCol>
        <!-- 用户信息行 -->
        <div class="d-flex align-center mb-1">
          <span class="font-weight-bold text-body-1">{{ props.comment.username }}</span>
          <VChip v-if="isAuthor" class="ml-2" color="primary" small>作者</VChip>
          <span class="ml-auto text-caption text-medium-emphasis">
            {{ formatTime(props.comment.createTime) }}
          </span>
        </div>

        <!-- 评论内容 -->
        <div class="text-body-1 mb-2">
          {{ props.comment.content }}
        </div>

        <!-- 操作按钮 -->
        <div class="d-flex">
          <VBtn
            color="primary"
            prepend-icon="mdi-reply"
            size="small"
            variant="text"
            @click="toReply= true"
          >
            回复
          </VBtn>

          <VBtn
            class="ml-2"
            color="secondary"
            prepend-icon="mdi-thumb-up-outline"
            size="small"
            variant="text"
          >
            点赞
          </VBtn>
        </div>
      </VCol>
    </VRow>
    <VRow>
      <VCol v-show="childLength > 0" cols="2"><VBtn @click="showChild= !showChild">显示更多{{ childLength }}</VBtn></VCol>
      <VCol v-if="toReply">
        <VTextarea
          v-model="commentContent"
          auto-row
          class="mb-4"
          placeholder="请输入评论"
          rows="1"
        >
          <template #append>
            <VBtn @click="reply">回复</VBtn>
          </template>
        </VTextarea>
      </VCol>
    </VRow>
    <VRow
      v-for="childComment in props.comment.childComment"
      :key="childComment.id"
    >
      <VCol v-if="showChild">
        <div class="d-flex align-center mb-1 ml-10 ">
          <VAvatar color="primary" size="48">
            <VImg
              alt="用户头像"
              :src=" 'https://cdn.vuetifyjs.com/images/john.jpg'"
            />
          </VAvatar>
          <span class="font-weight-bold text-body-1">{{ props.comment.username }}</span>
          <VChip v-if="isAuthor" class="ml-2" color="primary" small>作者</VChip>
          <span>{{ childComment.content }}</span>
          <span class="ml-auto text-caption text-medium-emphasis">
            {{ formatTime(childComment.createTime) }}
          </span>
        </div>
      </VCol>
    </VRow>
  </VCard>

</template>

  <script lang="ts" setup>
  import type { CommentResponse } from '@/types';
  import { useAppStore } from '@/stores/app';
  import { commentApi } from '@/api/comment';
  const store = useAppStore()
  const props = defineProps<{ comment: CommentResponse }>();
  const childLength = ref(props.comment.childComment.length)
  const isAuthor = computed(()=>{
    const judge = store.useId === props.comment.usersId
    return judge
  })
  const toReply = ref(false)
  const commentContent = ref('')
  const route = useRoute()
  const showChild = ref(false)
  onMounted(()=>{
    console.log(props.comment.childComment)
  })
  const reply = ()=>{
    try{
      toReply.value = false
      const articleId = (route.params as { id: number }).id
      const parentId = props.comment.id
      commentApi.createComment({
        content: commentContent.value,
        articleId,
        parentCommentId: parentId,
      })
    }catch(err){
      console.warn(err)
    }
  }

  // 时间格式化函数
  const formatTime = (time: string | Date) => {
    const date = new Date(time);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
  };


  </script>

<style scoped>
  /* 卡片悬停效果 */
  .v-card:hover {
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    transition: box-shadow 0.3s ease;
  }

  /* 优化间距 */
  .v-row {
    align-items: flex-start;
  }
  </style>
