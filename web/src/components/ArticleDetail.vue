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
      <v-col cols="11">
        <v-card class="mt-4 pa-10 rounded-lg ">
          <VParallax
            class="rounded-lg"
            height="700"
            :src="articleDetail.cover_url"
          >
            <template #placeholder>
              <div class="d-flex align-center justify-center fill-height">
                <v-progress-circular
                  color="grey-lighten-4"
                  indeterminate
                />
              </div>
            </template></VParallax>
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
  <VFadeTransition group>
    <VFab
      v-show="showFab"
      :key="showFab ? 'fab-visible' : 'fab-hidden'"
      app
      class="mb-8 mr-8"
      color="primary"
      icon="mdi-arrow-up"
      style="position: fixed"
      @click="scrollToTop"
    />
  </VFadeTransition>
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

  const showFab = ref(false);
  let lastScrollPosition = 0;

  // 滚动事件处理函数
  const handleScroll = () => {
    const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;

    // 向下滚动超过100px时显示FAB
    if (currentScrollPosition > 100 && currentScrollPosition > lastScrollPosition) {
      showFab.value = true;
    }
    // 滚动到顶部时隐藏FAB
    else if (currentScrollPosition < 50) {
      showFab.value = false;
    }

    lastScrollPosition = currentScrollPosition;
  };

  // 滚动到顶部函数
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
    showFab.value = false;
  };

  // 添加滚动监听
  onMounted(() => {
    const articleId = (route.params as { id: number }).id as number;
    if (articleId) fetchArticle(articleId);

    window.addEventListener('scroll', handleScroll);
  });

  // 移除滚动监听
  onBeforeUnmount(() => {
    window.removeEventListener('scroll', handleScroll);
  });
  // 统一获取文章方法
  const articleDetail = ref(defaultFactory.defaultArticleDetailResponse());
  const fetchArticle = async (id: number) => {
    try {
      const res = await articleApi.getArticleDetail(id) as ArticleDetailResponse;
      console.log(res)
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

:deep(.md-editor-preview) {
  font-size: 20px !important; /* 使用px单位更精确 */
  line-height: 1.8 !important;

}
.md-editor{
   background-color: #FFFBFE;
}
.md-editor-dark{
  background-color: #212121;
}
/* 单独调整代码块字体 */
:deep(.md-editor-code-block ) {
  font-size: 20px !important;
}

</style>
