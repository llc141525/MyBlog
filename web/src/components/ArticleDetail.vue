<template>
  <v-container class="pt-0" fluid>
    <v-row class="d-flex">
      <v-breadcrumbs class="px-4 pt-4 " :items="['Home', articleDetail.title]" style="left: 260px; position: relative;">
        <template #prepend>
          <v-icon icon="mdi-home" size="small" />
        </template>
      </v-breadcrumbs>
    </v-row>
    <v-row justify="center">
      <v-col cols="12" md="10">
        <v-card class="mt-4 pa-4 rounded-lg px-10">
          <v-card-title
            class="text-h3  font-weight-bold "
            style="text-align: center; font-weight: bold;"
          >
            {{ articleDetail.title }}
          </v-card-title>
          <VDivider class="my-4" />
          <MdPreview
            code-theme="github"
            :editor-id="id"
            :model-value="articleDetail.content"
            :theme="store.isDarkMode ? 'dark' : 'light'"
          />
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>

<script lang="ts" setup>
  import{ MdPreview } from 'md-editor-v3';
  import 'md-editor-v3/lib/preview.css';
  import { onMounted, ref, watch } from 'vue';
  const id = 'preview-only';


  import { useRoute } from 'vue-router';
  import { articleApi } from '@/api/article';
  import { useAppStore } from '@/stores/app';
  import type { ArticleDetailResponse } from '@/types';
  import { defaultFactory } from '@/types/factory';
  const route = useRoute();
  const store = useAppStore()
  const articleDetail = ref(defaultFactory.defaultArticleDetailResponse());
  // 统一获取文章方法
  const fetchArticle = async (id: number) => {
    try {
      const res = await articleApi.getArticleDetail(id) as ArticleDetailResponse;
      articleDetail.value = res;
    } catch(err) {
      console.error('文章加载失败', err);
    }
  };

  // 初始化加载
  onMounted(() => {
    const articleId = (route.params as { id : number }).id as number;
    if(articleId) fetchArticle(articleId);
  });

  // 监听路由变化
  watch(() => (route.params as { id:number }).id, newId => {
    if(newId) fetchArticle(Number(newId));
  });


</script>
<style scoped>
.md-editor-dark{
  background-color: #212121;
}
</style>
