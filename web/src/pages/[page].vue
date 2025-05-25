<template>
  <v-sheet> 主页</v-sheet>
</template>

<script lang="ts" setup>
  import http from '@/utils/axios.ts';

  http.get('/api/article/detail/4').then(res => console.log(res.data));

</script>


