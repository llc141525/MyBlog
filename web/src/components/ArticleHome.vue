<template>

  <VNavigationDrawer>
    <v-list-item subtitle="Vuetify" title="文章目录" />
    <v-divider />
    <v-list-item link title="List Item 1" />
    <v-list-item link title="List Item 2" />
    <v-list-item link title="List Item 3" />
  </VNavigationDrawer>

  <VBreadcrumbs :items="['Foo', 'Bar', 'Fizz']" />
  <v-main>
    <VCard>

      <v-parallax
        height="300"
        src="https://cdn.vuetifyjs.com/images/parallax/material2.jpg"
      />

      {{ articleDetail }}
    </VCard>
  </v-main>

</template>


<script lang="ts" setup>

  import { articleApi } from '@/api/article';

  import type { ArticleDetailResponse } from '@/types';

  import { defaultFactory } from '@/types/factory';

  const articleDetail = ref<ArticleDetailRes>(defaultFactory.defaultArticleDetailResponse())
  const article = async ()=>{
    try{
      const res = await articleApi.getArticleDetail(9) as ArticleDetailResponse
      articleDetail.value = res
    }catch(err){
      console.log(err)
    }
  }
  onMounted(()=>{
    article()
  })


</script>
