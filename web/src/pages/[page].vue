<template>
  <v-sheet> 主页 {{ page }}</v-sheet>
  <article-card />
</template>

<script lang="ts" setup>
  const route= useRoute()

  // 获取路由参数, page 是页数.
  const page= (route.params as { page: string }).page
  

</script>
