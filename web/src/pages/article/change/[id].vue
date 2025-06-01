<template>

  <div>
    <v-container class="pa-6">
      <v-card class="pa-6 rounded-lg" elevation="2">
        <!-- 编辑器区域 -->
        <div class="mb-6">
          <MdEditor
            id="md-editor"
            v-model="articleDetail.content"
            class="editor-content"
            :theme="store.isDarkMode ? 'dark' : 'light'"
          />
        </div>

        <v-text-field
          v-model="articleDetail.title"
          dense
          label="文章标题"
          outlined
        />

        <VTextarea
          v-model="articleDetail.summarize"
          :counter="100"
          dense
          label="文章概述(中文)"
          outlined
          :roules="roule"
        />


        <div class="mb-6">
          <v-file-input
            accept="image/*"
            label="上传封面"
            outlined
            prepend-icon="mdi-image"
            :show-size="true"
          />
        </div>

        <div class="text-center mt-4">
          <v-btn
            class="px-8"
            color="primary"
            @click="updateArticle"
          >
            发表文章
          </v-btn>
          <VBtn
            class="mx-3 px-8"
            color="secondary"
            text="暂存文章"
            @click="temUpdate"
          />
        </div>
      </v-card>

      <VSnackbar
        v-model="snackBar"
        text="文章更新成功"
        timeout="2000"
      />
    </v-container>
  </div>
</template>

<script setup lang="ts">
  import { articleApi } from '@/api/article';
  import { useAppStore } from '@/stores/app';
  import { type ArticleDetailResponse, defaultFactory } from '@/types';
  import { MdEditor } from 'md-editor-v3';
  import 'md-editor-v3/lib/style.css';
  const route = useRoute()
  const articleDetail = ref(defaultFactory.defaultArticleDetailResponse());
  const fetchArticle = async (id: number) => {
    try {
      const res = await articleApi.getArticleDetail(id) as ArticleDetailResponse;
      articleDetail.value = res;
    } catch(err) {
      console.error('文章加载失败', err);
    }
  };
  const store= useAppStore()

  onMounted(() => {
    const id = (route.params as { id : number }).id as number;
    fetchArticle(id);

  });
  const snackBar = ref(false);
  const router= useRouter();
  const updateArticle = async ()=>{
    try{
      snackBar.value = true;
      const update = defaultFactory.defaultUpdateArticle()
      update.articleId = articleDetail.value.id
      update.title = articleDetail.value.title
      update.content = articleDetail.value.content
      update.status = true
      update.summarize = articleDetail.value.summarize
      await articleApi.updateArticle(update)
      snackBar.value = false;
      router.push(`/article/${articleDetail.value.id}`)
    }catch(err){
      console.error(err)
    }
  }

  const temUpdate = async () => {
    try{
      snackBar.value = true;
      const update = defaultFactory.defaultUpdateArticle()
      update.articleId = articleDetail.value.id
      update.title = articleDetail.value.title
      update.content = articleDetail.value.content
      update.status = false
      update.summarize = articleDetail.value.summarize
      await articleApi.updateArticle(update)
    }catch(err){
      console.error(err)
    }
  }

  const roule = [
    (s : string) => s.length > 100 || '标题长度不能超过100个字符',
  ]
</script>
