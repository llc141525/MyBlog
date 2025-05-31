<template>
  <VCard class="mb-4 rounded-lg" elevation="1">
    <!-- 主评论区域 -->
    <VRow align="start" class="pa-4" no-gutters>
      <!-- 头像区域 -->
      <VCol class="mr-3" cols="auto">
        <VAvatar color="primary" size="48">
          <VImg alt="用户头像" src="https://cdn.vuetifyjs.com/images/john.jpg" />
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
        <div class="d-flex align-center">
          <VBtn
            color="primary"
            prepend-icon="mdi-reply"
            size="small"
            variant="text"
            @click="toReply = !toReply"
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

          <!-- 子评论切换按钮 -->
          <VBtn
            v-if="childLength > 0"
            class="ml-auto"
            :prepend-icon="showChild ? 'mdi-chevron-up' : 'mdi-chevron-down'"
            size="small"
            variant="text"
            @click="showChild = !showChild"
          >
            {{ showChild ? '收起回复' : `查看回复(${childLength})` }}
          </VBtn>
        </div>

        <!-- 回复输入框 -->
        <VExpandTransition>
          <div v-if="toReply" class="mt-3">
            <VTextarea
              v-model="commentContent"
              auto-grow
              hide-details
              placeholder="请输入回复内容"
              rows="2"
              variant="outlined"
            >
              <template #append>
                <VBtn variant="text" @click="toReply = false">取消</VBtn>
                <VBtn color="primary" @click="reply">发送</VBtn>
              </template>
            </VTextarea>
          </div>
        </VExpandTransition>
      </VCol>
    </VRow>

    <!-- 子评论区域 -->
    <VExpandTransition>
      <div v-if="showChild && childLength > 0" class="child-comments">
        <VDivider class="mx-4" />

        <div v-for="childComment in props.comment.childComment" :key="childComment.id" class="py-3 px-6">
          <VRow align="start" no-gutters>
            <!-- 子评论头像 -->
            <VCol class="mr-3" cols="auto">
              <VAvatar color="primary" size="36">
                <VImg alt="用户头像" src="https://cdn.vuetifyjs.com/images/john.jpg" />
              </VAvatar>
            </VCol>

            <!-- 子评论内容 -->
            <VCol>
              <div class="d-flex align-center mb-1">
                <span class="font-weight-bold text-body-2">{{ childComment.username }}</span>
                <VChip v-if="isAuthor" class="ml-2" color="primary" x-small>作者</VChip>
                <span class="ml-auto text-caption text-medium-emphasis">
                  {{ formatTime(childComment.createTime) }}
                </span>
              </div>

              <div class="text-body-2">
                {{ childComment.content }}
              </div>
            </VCol>
          </VRow>
        </div>
      </div>
    </VExpandTransition>
  </VCard>
</template>

  <script lang="ts" setup>
  import { computed, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import type { CommentResponse } from '@/types';
  import { useAppStore } from '@/stores/app';
  import { commentApi } from '@/api/comment';

  const store = useAppStore();
  const route = useRoute();
  const props = defineProps<{ comment: CommentResponse }>();

  const childLength = computed(() => props.comment.childComment?.length || 0);
  const isAuthor = computed(() => store.useId === props.comment.usersId);
  const toReply = ref(false);
  const commentContent = ref('');
  const showChild = ref(false);

  // 时间格式化函数
  const formatTime = (time: string | Date) => {
    const date = new Date(time);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
  };
  const emit = defineEmits(['replied'])
  const reply = async () => {
    try {
      const articleId = (route.params as { id: number }).id;
      const parentId = props.comment.id;

      await commentApi.createComment({
        content: commentContent.value,
        articleId,
        parentCommentId: parentId,
      });
      emit('replied')
      // 重置状态
      commentContent.value = '';
      toReply.value = false;
    } catch (err) {
      console.warn('回复失败:', err);
    }
  };
  </script>

  <style scoped>
  /* 卡片悬停效果 */
  .v-card:hover {
    box-shadow: 0 6px 16px rgba(0,0,0,0.12);
    transition: all 0.3s ease;
  }

  /* 子评论区域样式 */
  .child-comments {
    background-color: rgba(0, 0, 0, 0.02);
    border-radius: 0 0 8px 8px;
  }
  </style>
