<template>
  <VSheet>文章卡片</VSheet>
  <v-card>
    articleHome : <br>
    {{ articleHome }}
    <br>articleDetail<br>
    {{ articleDetail }}
    <br> comment <br>
    {{ comment }}
    <br> users<br>
    {{ user }}
  </v-card>
  <v-btn @click="getArticleHome"> 获取文章首页 </v-btn>

</template>

<script lang="ts" setup>
  import type { ArticleDetailResponse, ArticleHomeResponse, CommentResponse, UsersResponse } from '@/types';
  import {articleApi} from "@/api/article.ts";
  // 测试所有接口

  const articleHome = ref<ArticleHomeResponse>()
  const articleDetail = ref<ArticleDetailResponse>()
  const comment = ref<CommentResponse[]>()
  const user = ref<UsersResponse>()

  // onMounted(async () => {
  // // articleHome.value = await articleApi.getArticleHome(1)
  // // articleDetail.value = await articleApi.getArticleDetail(4)
  // // comment.value = await commentApi.getComment(4)
  // // user.value = await usersApi.login({ username: 'abc', password: '123' })
  // // await usersApi.logout();
  // // usersApi.register({ username: 'tttttffffft', password: 'sdfsdfs' })
  // // usersApi.update({ username: 'testtestttt' })
  // // commentApi.createComment({ content: '测试测试', articleId: 4 })
  // // commentApi.deleteComment(19)
  // // articleApi.createArticle({ title: 'title', content: 'content', cover_url: 'test', status: false })
  // // articleApi.deleteArticle(6)
  // // articleApi.updateArticle({ articleId: 9, content: '测试' })
  // })
  const getArticleHome = async ()=>{
    articleHome.value = await articleApi.getArticleHome(1)
  }

</script>
