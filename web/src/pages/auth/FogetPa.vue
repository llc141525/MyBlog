<template>
  <v-card
    class="mx-auto"
    elevation="10"
    max-width="450"
  >
    <v-toolbar color="primary" dark>
      <v-toolbar-title>重设密码</v-toolbar-title>
    </v-toolbar>
    <v-card-text>
      <v-form ref="resetForm">
        <v-text-field
          class="mb-4"
          clearable
          label="用户名"
          prepend-inner-icon="mdi-account"
          required
          :rules="[v => !!v || '账号不能为空']"
          variant="outlined"
        />

        <v-text-field
          v-model="newPassword"
          :append-icon="showPassword ? mdiEyeOff : mdiEye"
          class="mb-4"
          clearable
          label="新密码"
          prepend-inner-icon="mdi-lock"
          required
          :rules="[
            v => !!v || '密码不能为空',
            v => (v && v.length >= 6) || '密码长度至少为6位'
          ]"
          :type="showPassword ? 'text' : 'password'"
          variant="outlined"
          @click:append="showPassword = !showPassword"
        />

        <v-text-field
          :append-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
          class="mb-4"
          clearable
          label="确认新密码"
          prepend-inner-icon="mdi-lock"
          required
          :rules="[
            v => !!v || '请确认密码',
            v => v === newPassword || '两次输入的密码不一致'
          ]"
          :type="showConfirmPassword ? 'text' : 'password'"
          variant="outlined"
          @click:append="showConfirmPassword = !showConfirmPassword"
        />
        <v-btn
          block
          color="primary"
          :disabled="loading"
          :loading="loading"
          type="submit"
        >
          重设密码
        </v-btn>
      </v-form>
    </v-card-text>

    <v-card-actions>
      <v-spacer />
      <router-link to="/auth/login">
        <v-btn color="primary">返回登录</v-btn>
      </router-link>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts" setup>
  import { mdiEye, mdiEyeOff } from '@mdi/js';
  import { ref } from 'vue';
  const showConfirmPassword = ref(false)

  const newPassword = ref('')
  const showPassword = ref(false)
  const loading = ref(false)

</script>

<style scoped>
.mx-auto {
  margin-top: 5rem;
  border-radius: 12px;
  overflow: hidden;
}
</style>
