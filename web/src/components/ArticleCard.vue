<template>
  <v-card
    class="rounded-lg"
    :elevation="hover ? 8 : 2"
    height="500"
    @mouseleave="hover = false"
    @mouseover="hover = true"
  >
    <v-img
      cover
      gradient="to bottom, rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.2)"
      height="250"
      :src="props.article.cover_url? props.article.cover_url : `https://picsum.photos/500/300?random=${props.cnt}`"
    >
      <v-chip
        class="ma-2"
        color="primaryContainer"
        label
        prepend-icon="mdi-account"
        size="small"
      >
        {{ props.article.authorName }}
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

    <v-card-item class="px-5 mt-3">
      <v-card-title class="text-h6 mb-4" style="word-break: break-word; white-space: pre-wrap">
        {{ props.article.title }}
      </v-card-title>
      <v-card-subtitle
        class="text-medium-emphasis text-body-1"
        style="word-break: break-word; white-space: pre-wrap ; height: 75px;"
      >
        {{ article.summarize }}
      </v-card-subtitle>
    </v-card-item>
    <div>

      <v-btn
        append-icon="mdi-arrow-right"
        class="my-2 mx-6"
        color="secondary"
        :to="`/article/${props.id}`"
        variant="flat"
      >
        阅读更多
      </v-btn>

      <v-btn
        v-if="store.useId === props.article.usersId"
        append-icon="mdi-delete"
        color="error"
        @click="deletArticle"
      >
        删除
      </v-btn>
    </div>


    <div class="d-flex align-center justify-start text-medium-emphasis text-caption px-6 py-4">
      <v-icon class="me-1" icon="mdi-calendar" size="small" />
      <span class="me-4">{{ props.article.createTime.substring(0, 10) }}</span>

      <v-icon class="me-1" icon="mdi-comment" size="small" />
      <span class="me-4">{{ props.article.commentLength }}</span>

      <v-icon class="me-1" icon="mdi-eye" size="small" />
      <span>1.2k</span>
    </div>
  </v-card>
</template>

<script lang="ts" setup>
  import { useAppStore } from '@/stores/app';
  import { articleApi } from '@/api/article'
  const emit = defineEmits(['delete-article'])

  const props = defineProps<{ cnt: number, article:ArticleHomeRes, id:number }>();
  // console.log(props.article)
  const hover = ref(false);
  const store = useAppStore()
  // console.log(props.article.cover_url)

  const deletArticle = async ()=>{
    try{
      await articleApi.deleteArticle(props.id)
      emit('delete-article')
    }catch (e){
      console.log(e)
    }
  }
</script>

<style scoped>
.v-card {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.v-card:hover {
  transform: translateY(-4px);
}
</style>
