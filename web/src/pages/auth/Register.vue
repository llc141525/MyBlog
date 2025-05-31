<template>
  <div>

    <v-card class="mx-auto" elevation="10" max-width="450">
      <v-toolbar color="primary" dark>
        <v-toolbar-title>用户注册</v-toolbar-title>
      </v-toolbar>
      <v-card-text>

        <v-snackbar v-model="registerSuccess" color="success" timeout="2000">
          注册成功！正在跳转登录...
        </v-snackbar>
        <v-form ref="form" @submit.prevent="register">
          <v-text-field
            v-model="userVar.username"
            :autofocus="true"
            class="mb-4"
            clearable
            :error="formHasError"
            :error-messages="errorMessage"
            label="用户名"
            prepend-inner-icon="mdi-account"
            required
            :rules="usernameRules"
            :success="registerSuccess"
            variant="outlined"
          />

          <v-text-field
            v-model="userVar.password"
            :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            class="mb-4"
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

          <v-text-field
            v-model="confirmPassword"
            :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
            class="mb-4"
            :error="formHasError"
            :error-messages="errorMessage"
            label="确认密码"
            prepend-inner-icon="mdi-lock"
            required
            :rules="confirmPasswordRules"
            :type="showConfirmPassword ? 'text' : 'password'"
            variant="outlined"
            @click:append-inner="showConfirmPassword = !showConfirmPassword"
          />

          <div class="d-flex justify-space-between align-center mt-2">
            <v-checkbox
              v-model="rememberMe"
              density="compact"
              hide-details
              label="记住我"
            />

            <router-link to="/auth/Login">
              <v-btn color="primary" variant="text">已有账号？登录</v-btn>
            </router-link>
          </div>

          <v-btn
            block
            class="mt-6"
            color="primary"
            :loading="loading"
            size="large"
            type="submit"
          >
            注册
          </v-btn>
        </v-form>
      </v-card-text>

      <v-divider />

      <v-card-actions class="justify-center py-4">
        <span class="text-caption">注册即表示您同意我们的</span>
        <a class="text-caption ml-2 text-primary text-decoration-none" href="/auth/Agree">服务条款</a>
      </v-card-actions>
    </v-card>
  </div>

</template>

<script lang="ts" setup>
  import { ref } from 'vue'
  const confirmPassword = ref('')
  const rememberMe = ref(false)
  const showPassword = ref(false)
  const showConfirmPassword = ref(false)
  const loading = ref(false)
  const form = ref(null)
  const errorMessage= ref('')
  const formHasError = ref(false)
  const registerSuccess = ref(false)
  const router = useRouter()

  const usernameRules = [
    (v:string) => !!v || '用户名不能为空',
    (v:string) => (v && v.length >= 3) || '用户名至少3个字符',
  ]

  const passwordRules = [
    (v:string) => !!v || '密码不能为空',
    (v:string) => (v && v.length >= 6) || '密码至少6个字符',
  ]

  const confirmPasswordRules = [
    (v:string) => !!v || '请确认密码',
    (v:string) => v === userVar.value.password || '密码不匹配',
  ]

  import { usersApi } from '@/api/users';
  import { defaultFactory } from '@/types/factory.ts'
  import type { UsersRequest, UsersResponse } from '@/types'

  const userVar = ref<UsersRequest>(defaultFactory.defaultCreateUsers())
  const user = ref<UsersResponse>()
  const register = async () => {
    loading.value = true
    try{
      user.value = await usersApi.register(userVar.value)
      loading.value = false
      errorMessage.value = ''
      formHasError.value = false
      // userVar.value = defaultFactory.defaultUsers()
      registerSuccess.value = true
      router.push('/auth/Login')
    }catch(e){
      console.log(e)
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
