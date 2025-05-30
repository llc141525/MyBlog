<template>
  <VApp>
    <!-- 修复导航栏：添加 fixed 属性并移除冗余标签 -->
    <VAppBar app color="primary" fixed>  <!-- 关键修复点[3,4](@ref) -->
      <VToolbarTitle class="text-h5">我的博客</VToolbarTitle>
      <VSpacer />
      <VBtn :icon="mdiCogOutline" />
      <VDivider class="mx-2" vertical />
      <Avatar />
    </VAppBar>

    <!-- 主体内容区：添加布局容器 -->
    <v-main>
      <v-container fluid>  <!-- 流式容器适配滚动[4](@ref) -->
        <router-view />
      </v-container>
    </v-main>
  </VApp>
</template>

<script lang="ts" setup>
  import { mdiCogOutline } from '@mdi/js';
</script>
