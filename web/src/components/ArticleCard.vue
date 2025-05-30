<template>
  <v-card
    class="rounded-lg"
    :elevation="hover ? 8 : 2"
    @mouseleave="hover = false"
    @mouseover="hover = true"
  >
    <v-img
      cover
      gradient="to bottom, rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.2)"
      height="200"
      :src="`https://picsum.photos/500/300?random=${props.cnt}`"
    >
      <v-chip
        class="ma-2"
        color="primaryContainer"
        label
        :prepend-icon="mdiAccount"
        size="small"
      >
        {{ props.article.usersId }}
      </v-chip>
    </v-img>

    <v-card-item class="px-8 mt-5">
      <v-card-title class="text-h6 mb-4" style="word-break: break-word; white-space: pre-wrap">
        {{ props.article.title }}
      </v-card-title>
      <v-card-subtitle class="text-medium-emphasis text-body-1" style="word-break: break-word; white-space: pre-wrap">
        Vue 3.0带来了许多令人兴奋的新特性，包括Composition API、性能优化和改进的TypeScript支持...
      </v-card-subtitle>
    </v-card-item>

    <v-card-actions class="px-4">
      <v-btn
        :append-icon="mdiArrowRight"
        color="primary"
        size="small"
        to="/article/1"
        variant="text"
      >
        阅读更多
      </v-btn>

      <v-spacer />

      <div class="d-flex align-center text-medium-emphasis text-caption">
        <v-icon class="me-1" :icon="mdiCalendar" size="small" />
        <span class="me-4">{{ props.article.createTime }}</span>

        <v-icon class="me-1" :icon="mdiComment" size="small" />
        <span class="me-4">{{ props.article.commentLength }}</span>

        <v-icon class="me-1" :icon="mdiEye" size="small" />
        <span>1.2k</span>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts" setup>

  // import { mdiAccount, m diArrowRight, mdiCalendar, mdiComment, mdiEye } from '@mdi/js';
  // import type { ArticleHomeResponse } from '@/types';
  // import { ref } from 'vue';
  const props = defineProps<{ cnt: number, article:ArticleHomeRes }>();
  const hover = ref(false);

</script>

<style scoped>
.v-card {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.v-card:hover {
  transform: translateY(-4px);
}
</style>
