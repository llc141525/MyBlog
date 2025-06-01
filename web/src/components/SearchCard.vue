<template>
  <VCard class="search-card" elevation="5">
    <VCardTitle class="d-flex align-center py-4 px-5 bg-primary">
      <VIcon class="mr-3 text-white" icon="mdi-magnify" size="28" />
      <span class="text-h5 text-white">文章搜索</span>
    </VCardTitle>

    <VDivider />

    <VCardText class="pa-5">
      <VRow align="center">
        <VCol cols="12" md="8">
          <VAutocomplete
            v-model="selectedArticle"
            autofocus
            clearable
            density="comfortable"
            item-title="title"
            item-value="id"
            :items="suggestion"
            placeholder="输入文章标题或关键词..."
            return-object
            @update:model-value="searchQuery = $event?.title || ''"
          >
            <template #prepend-inner>
              <VIcon icon="mdi-text-box-search-outline" size="24" />
            </template>

            <!-- 自定义选项显示 -->
            <template #item="{ props, item }">
              <VListItem
                v-bind="props"
                :subtitle="`ID: ${item.raw.articleId}`"
                :title="item.raw.title"
              />
            </template>
          </VAutocomplete>
        </VCol>

        <VCol class="text-right" cols="12" md="4">
          <VBtn
            class="action-btn px-6 "
            color="primary"
            size="large"
            variant="flat"
            @click="gotoArticle"
          >
            跳转
            <VIcon end icon="mdi-arrow-right" size="24" />
          </VBtn>
        </VCol>
      </VRow>

      <!-- 热门搜索标签 -->
      <div v-if="hotSearches.length" class="mt-6">
        <span class="text-body-1 font-weight-medium mr-3">热门搜索：</span>
        <VChip
          v-for="(tag, index) in hotSearches"
          :key="index"
          class="mr-3 mb-3 cursor-pointer px-4 py-2"
          size="medium"
          @click="applySearch(tag)"
        >
          {{ tag }}
        </VChip>
      </div>
    </VCardText>
  </VCard>
</template>

  <script lang="ts" setup>
  import request from '@/utils/http';
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';

  const router = useRouter();
  const emit = defineEmits(['close-dialog']);

  const searchQuery = ref('');
  const hotSearches = ref(['Vue3', 'Vuetify', '前端优化', '响应式设计']);


  const applySearch = (tag: string) => {
    searchQuery.value = tag;
  };

  const gotoArticle = () => {
    if (searchQuery.value) {
      router.push(`/article/${selectedArticle.value?.articleId}`);
      emit('close-dialog');
    }
  };

  const selectedArticle = ref<{ articleId: number, title: string }>()
  type searchParm = {
    articleId : number,
    title : string
  }
  const suggestion = ref<searchParm[]>([])

  const getSearch = async ()=>{
    try{
      const res = await request.get('/article/all-article') as searchParm[]
      res.forEach(element => {
        suggestion.value.push({
          articleId : element.articleId,
          title : element.title,
        })
      });
    }catch(err){
      console.warn(err)
    }
  }

  onMounted(() => {
    getSearch()
  })

  </script>

  <style scoped>
  .search-card {
    width: 100%;
    max-width: 800px; /* 扩大卡片宽度 */
    border-radius: 16px; /* 更大的圆角 */
    overflow: hidden;
    position: fixed; /* 固定在屏幕中间 */
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1000;
  }

  .action-btn {
    transition: all 0.3s ease;
    letter-spacing: 1px;
    font-weight: 600;
    font-size: 1.1rem;
  }

  .action-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
  }

  .action-btn:active {
    transform: translateY(0);
  }

  .search-input {
    font-size: 1.1rem;
    padding: 12px 16px;
  }

  .v-card-text {
    padding: 24px !important; /* 增加内边距 */
  }
  </style>
