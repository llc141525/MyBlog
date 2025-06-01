<template>
  <VApp>
    <v-layout>
      <VAppBar
        app
        color="primary"
        elevation="2"
        fixed
        height="64"
        style="position: fixed;"
      >
        <div class="d-flex align-center">
          <VBtn class="text-h5 font-weight-bold" variant="text" @click="goHome">
            我的博客
          </VBtn>

          <VBtn
            class="ml-4"
            prepend-icon="mdi-magnify"
            variant="tonal"
            @click="dialog=true"
          >
            搜索
            <template #append>
              <VChip
                class="shortcut-chip"
                color="primary"
                variant="outlined"
              >
                ctrl + k
              </VChip>
            </template>
          </VBtn>
        </div>

        <VSpacer />

        <div class="d-flex align-center">
          <VBtn
            v-if="store.isLogin"
            class="mr-2"
            color="secondary"
            prepend-icon="mdi-pencil"
            variant="flat"
            @click="goWrite"
          >
            新建
          </VBtn>

          <!-- 工具功能区 -->
          <VBtn
            class="mr-1"
            icon="mdi-theme-light-dark"
            variant="text"
            @click="toggleTheme"
          />
          <VBtn
            class="mr-3"
            icon="mdi-cog-outline"
            variant="text"
            @click="goSettings"
          />

          <!-- 用户功能区 -->
          <VDivider class="mx-2" inset vertical />
          <Avatar
            v-if="store.isLogin"
            class="mr-2"
            size="36"
          />
          <VBtn
            v-else
            density="comfortable"
            text="登录"
            variant="text"
            @click="router.push('/auth/Login')"
          />
        </div>
      </VAppBar>

      <v-main>
        <v-container class="mt-12" fluid>
          <VSlideXTransition mode="out-in">
            <router-view :key="route.path" />
          </VSlideXTransition>
        </v-container>
      </v-main>
    </v-layout>

    <VDialog v-model="dialog">
      <SearchCard @close-dialog="dialog = false" />
    </VDialog>
  </VApp>
</template>

<script lang="ts" setup>
  const router = useRouter();
  const route = useRoute()
  // console.log(route.fullPath)
  const goHome = ()=>{
    router.push('/1')
  }
  const goWrite = ()=>{
    router.push('/article/Create')
  }
  const goSettings = ()=>{
    router.push('/settings')
  }
  import { useAppStore } from '@/stores/app';
  import { useTheme } from 'vuetify'
  const theme = useTheme()
  const store = useAppStore()
  const toggleTheme = () => {
    store.troggleDarkMode()
    theme.global.name.value = store.isDarkMode ? 'dark' : 'light'
    // localStorage.setItem('userTheme', theme.global.name.value) // 保存偏好
  }
  onMounted(() => {
    // 从store恢复主题
    if (store.isDarkMode !== (theme.global.name.value === 'dark')) {
      theme.global.name.value = store.isDarkMode ? 'dark' : 'light'
    }
  })
  const dialog = ref(false)

</script>

<style scoped>
/* 优化按钮悬停效果 */
.v-btn:hover {
  transform: translateY(-1px);
  transition: transform 0.2s ease;
}

/* 快捷键标签样式 */
.shortcut-chip {
  font-size: 0.65rem;
  padding: 0 6px;
  height: 20px;
  margin-left: 8px;
}

/* 导航栏高度适配 */
.v-toolbar {
  padding: 0 16px;
}
</style>
