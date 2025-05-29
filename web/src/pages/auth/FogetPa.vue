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
      <v-form ref="resetForm" @submit.prevent="handleResetPassword">
        <v-text-field
          v-model="account"
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
          :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
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
          v-model="confirmPassword"
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

<script>
  export default {
    name: 'ResetPasswordCard',
    data () {
      return {
        account: '',
        newPassword: '',
        confirmPassword: '',
        showPassword: false,
        showConfirmPassword: false,
        loading: false,
        resetForm: null,
      }
    },
    methods: {
      async handleResetPassword () {
        if (this.resetForm.validate()) {
          this.loading = true;
          try {
            this.$router.push('/auth/login');
            this.$toast.success('密码重设成功，请使用新密码登录');
          } catch (error) {
            console.error('重设密码失败:', error);
            this.$toast.error('重设密码失败，请稍后重试');
          } finally {
            this.loading = false;
          }
        }
      },
    },
  }
</script>

<style scoped>
.mx-auto {
  margin-top: 5rem;
  border-radius: 12px;
  overflow: hidden;
}
</style>
