<template>
  <!-- 导航条 -->
  <NavBar />
  <!-- 主体 -->
  <v-main>
    <router-view />
  </v-main>

</template>

<script lang="ts" setup>
//   const transitions = {
//     'fade': 'v-fade-transition',
//     'slide-x': 'v-slide-x-transition',
//     'slide-y': 'v-slide-y-transition',
//     'scale': 'v-scale-transition',
//   }
//   const getTransition = route => {
//     return transitions[route.meta.transition] || 'v-fade-transition'
//   }
</script>

<!-- // <style scoped>
// .transition-wrapper {
//   position: absolute;
//   width: 100%;
// }
//</style> -->
