<template>
  <v-card
    class="rounded-lg"
    :elevation="hover ? 8 : 2"
    height="520"
    @mouseleave="hover = false"
    @mouseover="hover = true"
  >
    <v-img
      cover
      gradient="to bottom, rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.2)"
      height="250"
      :src="props.article.cover_url? props.article.cover_url : `https://picsum.photos/500/300?random=${props.cnt}`"
    >

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
      <v-card-title class="text-h6 mb-4 pr-5 " style="word-break: break-word; ">
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

    </div>


    <div class="d-flex align-center justify-start text-medium-emphasis text-caption px-6 py-4">
      <v-icon class="me-1" icon="mdi-calendar" size="small" />
      <span class="me-4">{{ props.article.createTime.substring(0, 10) }}</span>

      <v-icon class="me-1" icon="mdi-comment" size="small" />
      <span class="me-4">{{ props.article.commentLength }}</span>

      <v-icon class="me-1" icon="mdi-eye" size="small" />
      <span>1.2k</span>
      <v-chip
          class="ma-2 ml-10"
          :color="chipColor()"
          label
          prepend-icon="mdi-account"
          size="small"
      >
        {{ props.article.authorName }}
      </v-chip>
      <v-chip
          v-if="!props.article.status"
          class="ma-2"
          color="secondary"
          label
          size="small"
      >
        暂存
      </v-chip>
    </div>
  </v-card>
</template>

<script lang="ts" setup>
  import { useAppStore } from '@/stores/app';

  const props = defineProps<{ cnt: number, article:ArticleHomeRes, id:number, userId:number }>();
  // console.log(props.article)
  const hover = ref(false);
  const store = useAppStore()
  function chipColor (){
    console.log(props.userId, store.useId)
    if(props.userId === store.useId){
      return 'primary'
    }else{
      return 'primaryContainer'
    }
  }
  console.log(chipColor())
</script>

<style scoped>
.v-card {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.v-card:hover {
  transform: translateY(-4px);
}
</style>
