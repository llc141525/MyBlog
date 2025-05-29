<template>
  <!-- 导航条 -->
  <VApp>
    <VAppBar>
      <VToolbar app fixed>
        <VToolbarTitle>我的博客</VToolbarTitle>
        <VSpacer />
        <VBtn :icon="mdiCogOutline" />
        <VDivider vertical />
        <Avatar />
      </VToolbar>
    </VAppBar>


    <!-- 主体 -->
    <v-main>
      <router-view />
    </v-main>
  </VApp>

</template>

<script lang="ts" setup>
  import { mdiCogOutline } from '@mdi/js';

</script>
