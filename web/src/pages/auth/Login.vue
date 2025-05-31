<template>
  <div>

    <v-card
      class="mx-auto"
      elevation="10"
      max-width="450"
    >
      <v-toolbar color="primary" dark>
        <v-toolbar-title>用户登录</v-toolbar-title>
      </v-toolbar>

      <v-card-text>
        <v-form ref="form" @submit.prevent="login">
          <v-text-field
            v-model="userVar.username"
            autofocus
            class="mb-4"
            clearable
            :error="formHasError"
            :error-messages="errorMessage"
            label="用户名"
            prepend-inner-icon="mdi-account"
            required
            :rules="usernameRules"
            variant="outlined"
          />
          <v-text-field
            v-model="userVar.password"
            :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            :error="formHasError"
            :error-messages="errorMessage"
            label="密码"
            prepend-inner-icon="mdi-lock"
            required
            :rules="passwordRules"
            :type="showPassword ? 'text' : 'password'"
            variant="outlined"
            @click:append-inner="showPassword = !showPassword"
          />

          <div class="d-flex justify-space-between align-center mt-2">
            <v-checkbox
              v-model="rememberMe"
              density="compact"
              hide-details
              label="记住我"
            />
            <router-link to="/auth/FogetPa">
              <v-btn color="primary" variant="text">忘记密码?</v-btn>
            </router-link>
          </div>

          <v-btn
            block
            class="mt-6"
            color="primary"
            :loading="loading"
            size="large"
            type="submit"
          >登录
          </v-btn>
        </v-form>
      </v-card-text>

      <v-divider />

      <v-card-actions class="justify-center py-3">
        <span class="text-caption"><h3>没有账号?</h3></span>
        <router-link to="/auth/Register">
          <v-btn text>立刻注册</v-btn>
        </router-link>
      </v-card-actions>
    </v-card>
  </div>

</template>

<script setup lang="ts">
  import { ref } from 'vue'
  const rememberMe = ref(false)
  const showPassword = ref(false)
  const loading = ref(false)


  const usernameRules = [
    (v:string) => !!v || '用户名不能为空',
    (v:string) => (v && v.length >= 3) || '用户名至少3个字符',
    (v:string) => (v && v.length <= 12) || '用户名最多12个字符',
  ]

  const passwordRules = [
    (v : string) => !!v || '密码不能为空',
    (v: string) => (v && v.length >= 6) || '密码至少6个字符',
  ]

  import type { UsersRequest, UsersResponse } from '@/types/index'
  import { defaultFactory } from '@/types/factory'
  import { usersApi } from '@/api/users'
  import { useAppStore } from '@/stores/app'

  const errorMessage = ref('')
  const formHasError = ref(false)
  const userVar = ref<UsersRequest>(defaultFactory.defaultUsers())
  const user = ref<UsersResponse | null>(null)
  const router = useRouter()
  const store = useAppStore()
  const login = async () => {
    loading.value = true
    try{
      user.value = await usersApi.login(userVar.value)
      store.login(user.value.id, user.value.username, user.value.avatarUrl)
      console.log(store.avatarUrl)
      router.push('/1')
    }catch(e){
      console.warn(e)
      errorMessage.value = (e as Error).message
      formHasError.value = true
      loading.value = false
    }
  }
</script>

<style scoped>
.mx-auto {
  margin-top: 5rem;
  border-radius: 12px;
  overflow: hidden;
}
</style>
