<template>
  <VSheet>测试接口接口</VSheet>
  <v-card>
    文章首页(第一页) : <br>
    {{ articleHome }}
    <br>阅读文章(第4篇)<br>
    {{ articleDetail }}
    <br> comment <br>
    {{ comment }}
    <br> users<br>
    {{ user }}
  </v-card>
  <v-btn @click="getArticleHome"> 获取文章首页</v-btn>
  <v-btn @click="getArticleDetail"> 获取文章详情</v-btn>
  <br>
<!--  <v-sheet>-->
<!--    <v-text-field label="文章标题"  v-model="createArticleVar?.title"></v-text-field>-->
<!--    <v-text-field label="文章内容"  v-model="createArticleVar?.content"></v-text-field>-->
<!--    <v-text-field label="文章封面"  v-model="createArticleVar?.cover_url"></v-text-field>-->
<!--    <v-text-field label="文章状态"  v-model="createArticleVar?.status"></v-text-field>-->
<!--    -->
<!--  <v-btn @click="createArticle"> 创建文章</v-btn>-->
<!--  </v-sheet>-->
  <v-btn @click="updateArticle"> 更新文章</v-btn>
  <v-btn @click="deleteArticle"> 删除文章</v-btn> i8i8i
  <v-btn @click="getComment"> 获取评论</v-btn>
  <v-btn @click="createComment"> 创建评论</v-btn>
  <v-btn @click="deleComment"> 删除评论</v-btn>
  <v-btn @click="login"> 登录</v-btn>
  <v-btn @click="register"> 注册</v-btn>
  <v-btn @click="logout"> 退出</v-btn>
  <v-btn @click="updateUser"> 更新用户</v-btn>

</template>

<script lang="ts" setup>
import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  CommentResponse, CreateArticleRequest,
  UpdateArticleRequest,
  UsersRequest,
  UsersResponse
} from '@/types';
import {articleApi} from "@/api/article.ts";
import {commentApi} from "@/api/comment.ts";
import {usersApi} from "@/api/users.ts";
// 测试所有接口
// 测试文章所用的值
const articleHome = ref<ArticleHomeResponse>()
const articleDetail = ref<ArticleDetailResponse>()
const updateArticleVar = ref<UpdateArticleRequest>()
const createArticleVar = ref<CreateArticleRequest>()

// 测试评论所用的值
const comment = ref<CommentResponse[]>()

// 测试用户所用的值
const updateUserVar = ref<UsersRequest>()
const user = ref<UsersResponse>()
// 文章有关接口
const getArticleHome = async () => {
  articleHome.value = await articleApi.getArticleHome(1)
}
const getArticleDetail = async () => {
  articleDetail.value = await articleApi.getArticleDetail(4)
}
const createArticle = async () => {
  await articleApi.createArticle({title: 'title', content: 'content', cover_url: 'test', status: false})
}
const updateArticle = async () => {
  await articleApi.updateArticle({articleId: 9, content: '测试', title:"测试标题"})
}
const deleteArticle = async () => {
  await articleApi.deleteArticle(6)
}

// 评论有关接口
const getComment = async () => {
  comment.value = await commentApi.getComment(4)
}
const deleComment = async () => {
  await commentApi.deleteComment(1)
}
const createComment = async () => {
  // 如果是嵌套评论的子评论. 那么需要填入 parentCommentId
  await commentApi.createComment({content: '测试测试', articleId: 4})
}

// 用户有关接口
const login = async () => {
  user.value = await usersApi.login({username: 'abc', password: '123'})
}
const register = async () => {
  await usersApi.register({username: 'testeeeeeee', password: 'sdfsdfs'})
}
const logout = async () => {
  await usersApi.logout();
}
const updateUser = async () => {
  await usersApi.update({username: 'testtestttt'})
}
</script>
