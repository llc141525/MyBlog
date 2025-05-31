<template>
  <v-app id="app">
    <v-layout>
      <v-navigation-drawer
        class="pa-0"
        permanent
        style="position: fixed ; top:60px"
        width="280"
      >
        <v-list-item class="py-4" subtitle="Vuetify" title="文章目录" />
        <v-divider />
        <MdCatalog class="pt-2" :editor-id="id" :scroll-element="scrollElement" />
      </v-navigation-drawer>
      <v-main>
        <ArticleDetail />
        <v-container class="pt-0" fluid>
          <v-row justify="center">
            <v-col cols="12" md="10">
              <v-card class="mt-4 pa-4 rounded-lg px-10">
                <VCardTitle><VIcon icon="mdi-comment-text-outline" /> 评论</VCardTitle>

                <VTextarea
                  v-model="commentContent"
                  auto-grow
                  placeholder="请输入评论"
                  rows="1"
                >

                  <template #append>
                    <v-btn @click="createComment">提交</v-btn>
                  </template>
                </VTextarea>
                <div v-if="loading" class="text-center py-4">
                  <VProgressCircular color="primary" indeterminate />
                  <div class="mt-2">加载评论中...</div>
                </div>

                <div v-else-if="comments.length">
                  <Comment
                    v-for="comment in comments"
                    :key="comment.id"
                    :comment="comment"
                  />
                </div>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-main>
    </v-layout>
  </v-app>
</template>


<script lang="ts" setup>
  import ArticleDetail from '@/components/ArticleDetail.vue';
  import { MdCatalog } from 'md-editor-v3';
  const scrollElement = document.documentElement;
  const id = 'preview-only';
  import { commentApi } from '@/api/comment';
  import { type CommentResponse } from '@/types';
  const loading = ref(false)
  const route = useRoute()

  const comments = ref<CommentResponse[]>([])
  const getComment = async ()=>{
    try{
      loading.value = true
      const id = (route.params as { id: number }).id
      const res = await commentApi.getComment(id)
      comments.value = res
      console.log(comments.value)
      console.log(`comments: ${comments.value}`)
      loading.value = false
    }catch(err){
      console.log(err)
    }
  }

  const createComment = async ()=>{
    try{
      loading.value = true
      commentContent.value = ''
      const id = (route.params as { id: number }).id
      const res = await commentApi.createComment({
        content: commentContent.value,
        articleId: id,
      })
      console.log(res)
      getComment()
      loading.value = false
    }catch(err){
      console.log(err)
    }
  }

  onMounted(()=>{
    getComment()
  })
  onActivated(()=>{
    getComment()
  })
  const commentContent = ref('')

</script>
