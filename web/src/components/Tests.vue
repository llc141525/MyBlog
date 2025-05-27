<template>

  <!-- 数据展示区 -->
  <v-card class="ma-4 pa-4" elevation="2">
    <h2 class="mb-4">响应数据预览</h2>
    <v-sheet class="d-flex flex-wrap gap-4">
      <v-card class="pa-4 flex-grow-1" variant="outlined">
        <h3>文章数据</h3>
        <pre>{{ { articleHome, articleDetail } }}</pre>
      </v-card>
      <v-card class="pa-4 flex-grow-1" variant="outlined">
        <h3>评论数据</h3>
        <pre>{{ comment }}</pre>
      </v-card>
      <v-card class="pa-4 flex-grow-1" variant="outlined">
        <h3>用户数据</h3>
        <pre>{{ user }}</pre>
      </v-card>
    </v-sheet>
  </v-card>
  <!-- 用户模块 -->
  <v-card class="ma-4 pa-4" elevation="2">
    <h2 class="mb-4">用户模块</h2>
    <v-sheet class="d-flex flex-wrap gap-4">
      <!-- 登录/注册 -->
      <v-card class="pa-4" variant="outlined">
        <v-text-field v-model="userVar.username" density="compact" label="用户名" />
        <v-text-field v-model="userVar.password" density="compact" label="密码" type="password" />
        <div class="d-flex gap-2 mt-2">
          <v-btn color="primary" density="compact" @click="login">登录</v-btn>
          <v-btn density="compact" @click="register">注册</v-btn>
        </div>
      </v-card>

      <!-- 用户操作 -->
      <v-card class="pa-4" variant="outlined">
        <v-text-field v-model="updateUserVar.username" density="compact" label="新用户名" />
        <v-text-field v-model="updateUserVar.password" density="compact" label="新密码" />
        <div class="d-flex gap-2 mt-2">
          <v-btn color="warning" density="compact" @click="updateUser">更新用户</v-btn>
          <v-btn color="error" density="compact" @click="logout">退出登录</v-btn>
        </div>
      </v-card>
    </v-sheet>
  </v-card>

  <!-- 文章模块 -->
  <v-card class="ma-4 pa-4" elevation="2">
    <h2 class="mb-4">文章模块</h2>
    <v-sheet class="d-flex flex-wrap gap-4">
      <!-- 创建/更新 -->
      <v-card class="pa-4 flex-grow-1" style="min-width: 300px" variant="outlined">
        <h3 class="mb-2">{{ updateArticleVar.articleId ? '更新文章' : '新建文章' }}</h3>
        <v-text-field v-model="createArticleVar.title" density="compact" label="标题" />
        <v-text-field v-model="createArticleVar.content" density="compact" label="内容" />
        <v-text-field v-model="createArticleVar.cover_url" density="compact" label="封面" />
        <div class="d-flex gap-2 mt-2">
          <v-btn color="primary" density="compact" @click="createArticle">提交创建</v-btn>
          <v-btn color="warning" density="compact" @click="updateArticle">提交更新</v-btn>
        </div>
      </v-card>

      <!-- 文章操作 -->
      <v-card class="pa-4" variant="outlined">
        <div class="d-flex gap-2 align-center">
          <v-text-field v-model="deleteArticleId" density="compact" label="删除ID" style="width: 120px" />
          <v-btn color="error" density="compact" @click="deleteArticle">删除文章</v-btn>
        </div>
        <v-divider class="my-4" />
        <div class="d-flex gap-2">
          <v-btn density="compact" @click="getArticleHome">获取首页</v-btn>
          <v-text-field v-model="getArticleDetailId" density="compact" label="文章ID" style="width: 120px" />
          <v-btn density="compact" @click="getArticleDetail">获取详情</v-btn>
        </div>
      </v-card>
    </v-sheet>
  </v-card>

  <!-- 评论模块 -->
  <v-card class="ma-4 pa-4" elevation="2">
    <h2 class="mb-4">评论模块</h2>
    <v-sheet class="d-flex flex-wrap gap-4">
      <v-card class="pa-4 flex-grow-1" variant="outlined">
        <v-text-field v-model="articleIdForComment" density="compact" label="文章ID" />
        <v-text-field v-model="createCommentVar.content" density="compact" label="评论内容" />
        <div class="d-flex gap-2 mt-2">
          <v-btn color="primary" density="compact" @click="createComment">创建评论</v-btn>
          <v-btn density="compact" @click="getComment">获取评论</v-btn>
        </div>
      </v-card>

      <v-card class="pa-4" variant="outlined">
        <v-text-field v-model="deleteCommentId" density="compact" label="删除ID" />
        <v-btn color="error" density="compact" @click="deleComment">删除评论</v-btn>
      </v-card>
    </v-sheet>
  </v-card>

</template>

<script lang="ts" setup>
  import type {
    ArticleDetailResponse,
    ArticleHomeResponse,
    CommentResponse,
    CreateArticleRequest,
    CreateCommentRequest,
    UpdateArticleRequest,
    UsersRequest,
    UsersResponse,
  } from '@/types';
  import { articleApi } from '@/api/article.ts';
  import { commentApi } from '@/api/comment.ts';
  import { usersApi } from '@/api/users.ts';
  import { defaultFactory } from '@/types/factory.ts'
  // 测试所有接口
  // 测试用户所用的值
  const updateUserVar = ref<UsersRequest>(defaultFactory.defaultUpdateUser({}))
  const userVar = ref<UsersRequest>(defaultFactory.defaultUsers())
  const user = ref<UsersResponse>()


  // 用户有关接口
  const login = async () => {
    user.value = await usersApi.login(userVar.value)
  }
  const register = async () => {
    await usersApi.register(userVar.value)
  }
  const logout = async () => {
    await usersApi.logout();
  }
  const updateUser = async () => {
    await usersApi.update({ username: 'testtestttt' })
  }


  // 测试文章所用的值
  const articleHome = ref<ArticleHomeResponse>()
  const articleDetail = ref<ArticleDetailResponse>()
  const createArticleVar = ref<CreateArticleRequest>(defaultFactory.createDefaultArticle({}))
  const updateArticleVar = ref<UpdateArticleRequest>(defaultFactory.defaultUpdateArticle({}))
  const deleteArticleId = ref(0)
  const getArticleDetailId = ref(1)

  // 文章有关接口
  const getArticleHome = async () => {
    // 这里的参数是页数
    articleHome.value = await articleApi.getArticleHome(1)
  }
  // 参数: 文章id
  const getArticleDetail = async () => {
    articleDetail.value = await articleApi.getArticleDetail(getArticleDetailId.value)
  }
  const createArticle = async () => {
    await articleApi.createArticle(createArticleVar.value)
  }
  const updateArticle = async () => {
    await articleApi.updateArticle(updateArticleVar.value)
  }
  const deleteArticle = async () => {
    await articleApi.deleteArticle(deleteArticleId.value)
  }


  // 测试评论所用的值
  const comment = ref<CommentResponse[]>()

  // 评论有关接口

  const createCommentVar = ref<CreateCommentRequest>(defaultFactory.createDefaultComment({}))
  const articleIdForComment = ref(1)
  const deleteCommentId = ref(2)
  const getComment = async () => {
    comment.value = await commentApi.getComment(articleIdForComment.value)
  }
  const deleComment = async () => {
    await commentApi.deleteComment(deleteCommentId.value)
  }
  const createComment = async () => {
    // 如果是嵌套评论的子评论. 那么需要填入 parentCommentId
    await commentApi.createComment(createCommentVar.value)
  }


</script>
