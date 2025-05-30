<template>

  <v-main>
    <div class="content-container ">
      <div class="articles-grid">
        <v-row>
          <v-col
            v-for="(article, n) in articles"
            :key="n"
            class="mt-9"
            cols="12"
            lg="4"
            md="6"
          >
            <ArticleCard :id="article.id" :article="article" :cnt="article.id" />
          </v-col>
        </v-row>
      </div>

      <v-pagination
        v-model="page"
        circle
        class="my-10"
        color="primary"
        :length="totalPage"
        total-visible="5"
      />
    </div>

    <!-- {{ articleHome }} -->

  </v-main>


  <v-footer class="text-center d-flex flex-column ga-2 py-4" color="primary">
    <v-divider class="my-2" thickness="2" width="50" />

    <div class="text-caption font-weight-regular opacity-60">
      知识探索者博客致力于分享前沿技术、设计理念和生活感悟。我们相信知识的分享能够改变世界，让每个人都能在技术的浪潮中不断成长。
    </div>

    <v-divider />

    <div>
      {{ new Date().getFullYear() }} — <strong>关于我们</strong>
    </div>
  </v-footer>

</template>

<script lang="ts" setup>

  import type { ArticleHomeResponse, Pageable } from '@/types';
  import { articleApi } from '@/api/article.ts';

  const route = useRoute()
  const router = useRouter()
  const articleHome = ref<Pageable<ArticleHomeResponse>>({
    data: [], // 初始化空数据
    totalPages: 0,
    currentPage: 1,
    totalElements: 0,
    pageSize: 10,
  })

  const totalPage = ref(0)
  const articles = ref<ArticleHomeResponse[]>([])
  // 获取路由参数, page 是页数.
  const page = ref(Number((route.params as { page: string }).page) || 1)
  const getArticleHome = async () => {
    // 这里的参数是页数
    const res = await articleApi.getArticleHome(page.value)
    articleHome.value = {
      ...articleHome.value,
      ...res,
    }
    articles.value = articleHome.value.data
    totalPage.value = articleHome.value.totalPages
    console.log(articleHome.value.totalPages)
    // console.log('articleHome:')
    // console.log(articleHome.value.data)
    // console.log('articles:')
    // console.log(articles.value)
  }
  onMounted(async () => {
    await getArticleHome()
  })

  let isUpdating = false;
  watch(page, async newVal => {
    if (isUpdating) return;
    isUpdating = true;

    await router.push(`/${newVal}`);
    await getArticleHome();

    isUpdating = false;
  }, { immediate: true })
</script>
<style scoped>


.pagination-container {
  display: flex;
  justify-content: center;
  margin: 40px 0;
}


.footer-links a {
  color: #ddd;
  margin-bottom: 10px;
  text-decoration: none;
}

.footer-links a:hover {
  color: #3498db;
}


.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
