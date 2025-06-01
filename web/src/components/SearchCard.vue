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
            v-model="searchQuery"
            class="search-input"
            clearable
            density="comfortable"
            hide-details
            :items="suggestions"
            placeholder="输入文章标题或关键词..."
            variant="outlined"
            @update:search="fetchSuggestions"
          >
            <template #prepend-inner>
              <VIcon icon="mdi-text-box-search-outline" size="24" />
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
          variant="outlined"
          @click="applySearch(tag)"
        >
          {{ tag }}
        </VChip>
      </div>
    </VCardText>
  </VCard>
</template>

  <script lang="ts" setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';

  const router = useRouter();
  const emit = defineEmits(['close-dialog']);

  const searchQuery = ref('');
  const suggestions = ref<string[]>([]);
  const hotSearches = ref(['Vue3', 'Vuetify', '前端优化', '响应式设计']);

  const fetchSuggestions = (query: string) => {
    if (query.length > 1) {
      suggestions.value = [
        `${query}教程`,
        `${query}最佳实践`,
        `${query}高级技巧`,
        `${query}常见问题`,
      ];
    }
  };

  const applySearch = (tag: string) => {
    searchQuery.value = tag;
  };

  const gotoArticle = () => {
    if (searchQuery.value) {
      router.push('/article/10026');
      emit('close-dialog');
    }
  };
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
