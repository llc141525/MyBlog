<template>
  <VApp>
    <v-layout>
      <VAppBar app color="primary" fixed style="position: fixed;">
        <VToolbarTitle>
          <VBtn class="text-h5" variant="text" @click="goHome">
            我的博客
          </VBtn>
        </VToolbarTitle>
        <VTextField
          class="mt-5 ml-1"
          max-width="200"
          placeholder="搜索文章"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
        />

        <VSpacer class="ml-10" />
        <VBtn
          :disabled="!store.isLogin"
          prepend-icon="mdi-pencil"
          text="新建文章"
          @click="goWrite"
        />
        <VBtn
          class="mr-2"
          icon="mdi-theme-light-dark"
          @click="toggleTheme"
        />
        <VBtn class="mr-2" icon="mdi-cog-outline" @click="goSettings" />
        <VDivider class="mx-2" vertical />
        <Avatar
          v-if="store.isLogin"
          class="ma-3"
        />
        <VBtn
          v-else
          text="登录/注册"
          variant="text"
          @click="router.push('/auth/Login')"
        />
      </VAppBar>

      <v-main>
        <v-container fluid>
          <VSlideXTransition mode="out-in">
            <router-view :key="route.path" />
          </VSlideXTransition>
        </v-container>
      </v-main>
    </v-layout>
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

</script>
