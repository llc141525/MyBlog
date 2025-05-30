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
      height="250"
      :src="`https://picsum.photos/500/300?random=${props.cnt}`"
    >
      <v-chip
        class="ma-2"
        color="primaryContainer"
        label
        prepend-icon="mdi-account"
        size="small"
      >
        {{ props.article.usersId }}
      </v-chip>
      <template #placeholder>
        <div class="d-flex align-center justify-center fill-height">
          <v-progress-circular
            color="grey-lighten-4"
            indeterminate
          />
        </div>
      </template>
    </v-img>

    <v-card-item class="px-8 mt-3">
      <v-card-title class="text-h6 mb-4" style="word-break: break-word; white-space: pre-wrap">
        {{ props.article.title }}
      </v-card-title>
      <v-card-subtitle class="text-medium-emphasis text-body-1" style="word-break: break-word; white-space: pre-wrap">
        Vue 3.0带来了许多令人兴奋的新特性，包括Composition API、性能优化和改进的TypeScript支持...
      </v-card-subtitle>
    </v-card-item>

    <v-btn
      append-icon="mdi-arrow-right"
      class="my-2 mx-8"
      color="primary"
      :to="`/article/${props.id}`"
      variant="flat"
    >
      阅读更多
    </v-btn>


    <div class="d-flex align-center text-medium-emphasis text-caption px-8 py-4">
      <v-icon class="me-1" icon="mdi-calendar" size="small" />
      <span class="me-4">{{ props.article.createTime }}</span>

      <v-icon class="me-1" icon="mdi-comment" size="small" />
      <span class="me-4">{{ props.article.commentLength }}</span>

      <v-icon class="me-1" icon="mdi-eye" size="small" />
      <span>1.2k</span>
    </div>
  </v-card>
</template>

<script lang="ts" setup>


  // import { mdiAccount, mdiArrowRight, mdiCalendar, mdiComment, mdiEye } from '@mdi/js';

  const props = defineProps<{ cnt: number, article:ArticleHomeRes, id:number }>();
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
