<template>
  <v-sheet> 主页 {{ page }}</v-sheet>
  <test/>
</template>

<script lang="ts" setup>
  import Test from "@/components/Test.vue";

  const route= useRoute()

  // 获取路由参数, page 是页数.
  const page= (route.params as { page: string }).page


</script>
