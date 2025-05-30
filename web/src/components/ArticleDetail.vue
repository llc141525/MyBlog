<template>
  <v-app id="app">
    <v-layout>
      <v-navigation-drawer
        class="pa-0"
        height="100vh"
        permanent
        style="position: fixed;"
        width="280"
      >
        <v-list-item class="py-4" subtitle="Vuetify" title="文章目录" />
        <v-divider />
        <MdCatalog class="pt-2" :editor-id="id" :scroll-element="scrollElement" />
      </v-navigation-drawer>

      <v-main class="ma-0 pa-0">
        <v-container class="pt-0" fluid>
          <v-row justify="start">
            <v-breadcrumbs class="px-4 pt-4" :items="['Foo', 'Bar', 'Fizz']" />
          </v-row>
          <v-row justify="center">
            <v-col cols="12" lg="8" md="10">
              <v-card class="mt-4 pa-4 rounded-lg">
                <MdPreview :editor-id="id" :model-value="articleDetail.content" />
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script lang="ts" setup>
  import { articleApi } from '@/api/article';
  import type { ArticleDetailResponse } from '@/types';
  import { defaultFactory } from '@/types/factory';
  import { MdCatalog, MdPreview } from 'md-editor-v3';
  import 'md-editor-v3/lib/preview.css';
  import { onMounted, ref, watch } from 'vue';
  import { useRoute } from 'vue-router';

  const id = 'preview-only';
  const route = useRoute();
  const scrollElement = document.documentElement;
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
