<template>
  <div>

    <MdEditor
      id="md-editor"
      v-model="text"
      :theme="store.isDarkMode ? 'dark' : 'light'"
    />
    <v-text-field v-model="title" placeholder="文章标题" />
    <VFileInput
      v-model="cover"
      accept="image/*"
      label="上传封面"
      :show-size="true"
    />
    <v-btn text="提交文章" @click="createArticle" />

    <VSnackbar
      v-model="snackBar"
      text="文章创建成功"
      timeout="2000"
    />
  </div>

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
  const cover = ref(File);

  const createArticle = async () =>{
    try{
      createArticleRequest.value.content = text.value
      createArticleRequest.value.title = title.value
      createArticleRequest.value.status = status.value
      createArticleRequest.value.cover = cover.value
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


</script>

<style scoped>
:deep(.md-editor-preview) {
  font-size: 18px !important; /* 使用px单位更精确 */
  line-height: 1.8 !important;
}
</style>
