<template>
  <v-card class="mx-auto" elevation="10" max-width="450">
    <v-toolbar color="primary" dark>
      <v-toolbar-title>用户注册</v-toolbar-title>
    </v-toolbar>
    <v-card-text>
      <v-form ref="form" @submit.prevent="register">
        <v-text-field
          v-model="username"
          class="mb-4"
          clearable
          label="用户名"
          :prepend-inner-icon="mdiAccount"
          required
          :rules="usernameRules"
          variant="outlined"
        />

        <v-text-field
          v-model="password"
          :append-inner-icon="showPassword ? mdiEyeOff : mdiEye"
          label="密码"
          :prepend-inner-icon="mdiLock"
          required
          :rules="passwordRules"
          :type="showPassword ? 'text' : 'password'"
          variant="outlined"
          @click:append-inner="showPassword = !showPassword"
        />

        <v-text-field
          v-model="confirmPassword"
          :append-inner-icon="showConfirmPassword ? mdiEyeOff : mdiEye"
          label="确认密码"
          :prepend-inner-icon="mdiLock"
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
            <v-btn color="primary">已有账号？登录</v-btn>
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
</template>

<script lang="ts" setup>
  import { ref } from 'vue'
  import { mdiAccount, mdiEye, mdiEyeOff, mdiLock } from '@mdi/js';

  const username = ref('')
  const password = ref('')
  const confirmPassword = ref('')
  const rememberMe = ref(false)
  const showPassword = ref(false)
  const showConfirmPassword = ref(false)
  const loading = ref(false)
  const form = ref(null)

  const usernameRules = [
    v => !!v || '用户名不能为空',
    v => (v && v.length >= 3) || '用户名至少3个字符',
  ]

  const passwordRules = [
    v => !!v || '密码不能为空',
    v => (v && v.length >= 6) || '密码至少6个字符',
  ]

  const confirmPasswordRules = [
    v => !!v || '请确认密码',
    v => v === password.value || '密码不匹配',
  ]

  const register = async () => {
    const { valid } = await form.value.validate()
    if (!valid) return

    loading.value = true

    setTimeout(() => {
      console.log('注册信息:', {
        username: username.value,
        password: password.value,
        rememberMe: rememberMe.value,
      })
      loading.value = false
      alert('注册成功！')
    }, 1500)
  }
</script>

<style scoped>
.mx-auto {
  margin-top: 5rem;
  border-radius: 12px;
  overflow: hidden;
}
</style>
