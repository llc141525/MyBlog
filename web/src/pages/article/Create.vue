<template>
  <v-container class="pa-6"> <!-- 增加整体内边距 -->
    <v-card class="pa-6 rounded-lg" elevation="2">
      <!-- 编辑器区域 -->
      <div class="mb-6">
        <MdEditor
          id="md-editor"
          v-model="text"
          class="editor-content"
          :theme="store.isDarkMode ? 'dark' : 'light'"
        />
      </div>

      <!-- 表单区域使用网格布局 -->
      <v-row class="mb-4" :gutter="16">
        <v-col cols="12" md="6">
          <v-text-field
            v-model="title"
            dense
            label="文章标题"
            outlined
          />
        </v-col>
        <v-col cols="12" md="6">
          <v-text-field
            v-model="summarize"
            dense
            label="文章概述"
            outlined
          />
        </v-col>
      </v-row>

      <!-- 文件上传单独成组 -->
      <div class="mb-6">
        <v-file-input
          v-model="cover"
          accept="image/*"
          label="上传封面"
          outlined
          prepend-icon="mdi-image"
          :show-size="true"
        />
      </div>

      <!-- 按钮居中并增加间距 -->
      <div class="text-center mt-4">
        <v-btn
          class="px-8"
          color="primary"
          @click="createArticle"
        >
          提交文章
        </v-btn>
      </div>
    </v-card>

    <VSnackbar
      v-model="snackBar"
      text="文章创建成功"
      timeout="2000"
    />
  </v-container>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  // 引用screenfull
  // import screenfull from 'screenfull';
  import { MdEditor } from 'md-editor-v3';
  import 'md-editor-v3/lib/style.css';
  import { articleApi } from '@/api/article';
  import type { CreateArticleRequest } from '../../types/businessType';
  import { defaultFactory } from '@/types';
  import { useAppStore } from '@/stores/app';
  const title = ref('')
  const status = ref(false)
  const store = useAppStore()
  const snackBar = ref(false)
  const createArticleRequest = ref<CreateArticleRequest>(defaultFactory.defaultCreateArticle())
  const text = ref('');
  const cover = ref<File | null>(null);

  const createArticle = async () =>{
    try{
      createArticleRequest.value.content = text.value
      createArticleRequest.value.title = title.value
      createArticleRequest.value.status = status.value
      createArticleRequest.value.cover = cover.value
      createArticleRequest.value.summarize = summarize.value
      await articleApi.createArticle({
        ...createArticleRequest.value,
      })
      snackBar.value = true
      createArticleRequest.value = defaultFactory.defaultCreateArticle()
      text.value = ''
      title.value = ''
    }catch(err){
      console.error(err)
    }
  }
  const summarize = ref('')

</script>

<style scoped>
.editor-content {
  border: 1px solid rgba(0,0,0,0.1); /* 添加边框 */
  border-radius: 4px; /* 圆角 */
  min-height: 400px; /* 最小高度 */
}

/* 响应式调整 */
@media (max-width: 960px) {
  .pa-6 {
    padding: 24px !important;
  }
}
:deep(.md-editor-preview) {
  font-size: 18px !important;
  line-height: 1.8 !important;
}
</style>
