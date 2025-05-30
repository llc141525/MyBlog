<template>
  <v-text-field v-model="title" />
  <MdEditor v-model="text" />
  <v-btn text="提交文章" @click="createArticle" />
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
  const title = ref('')
  const status = ref(false)
  const createArticleRequest = ref<CreateArticleRequest>(defaultFactory.defaultCreateArticle())
  const text = ref('');
  const createArticle = async () =>{
    try{
      createArticleRequest.value.content = text.value
      createArticleRequest.value.title = title.value
      createArticleRequest.value.status = status.value

      const res = await articleApi.createArticle({
        ...createArticleRequest.value,
      })
      console.log(res)
    }catch(err){
      console.error(err)
    }
  }


</script>
