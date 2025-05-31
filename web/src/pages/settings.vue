<template>
  <v-container class="form-container" fluid>
    <!-- 头像上传区域 -->
    <v-row align="center" class="mb-4">
      <v-col cols="12" md="8">
        <v-file-input
          v-model="avatar"
          accept="image/*"
          density="comfortable"
          hide-details="auto"
          label="上传头像"
          outlined
          prepend-icon="mdi-camera"
        />
      </v-col>
      <v-col cols="12" md="4">
        <v-btn
          block
          class="mt-md-0 mt-2"
          color="primary"
          :disabled="!avatar"
          @click="updateAvatar"
        >
          <v-icon left>mdi-upload</v-icon>
          更新头像
        </v-btn>
      </v-col>
    </v-row>

    <!-- 分隔线 -->
    <v-divider class="my-6" />

    <!-- 用户信息表单 -->
    <v-form @submit.prevent="updateUser">
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            v-model="username"
            density="comfortable"
            hide-details="auto"
            label="用户名"
            outlined
            prepend-icon="mdi-account"
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="password"
            density="comfortable"
            hide-details="auto"
            label="密码"
            outlined
            prepend-icon="mdi-lock"
            type="password"
          />
        </v-col>
      </v-row>

      <!-- 提交按钮 -->
      <v-row class="mt-6">
        <v-col cols="12">
          <v-btn
            block
            color="primary"
            size="large"
            type="submit"
          >
            <v-icon left>mdi-send</v-icon>
            更新个人信息
          </v-btn>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

  <script setup lang="ts">
  import { usersApi } from '@/api/users';
  import { useAppStore } from '@/stores/app';
  import request from '@/utils/http';
  const store= useAppStore()

  const avatar = ref(File)
  const updateAvatar = async ()=>{
    try{
      const path:string = await request.post('/user/upload', {
        file:avatar.value,
      }, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      store.changeAvatar(path)
      console.log(path)
    }catch(err){
      console.log(err)
    }
  }

  const username = ref('')
  const password = ref('')

  const updateUser = async ()=>{
    try{
      await usersApi.update( { username:username.value,password:password.value })
      console.log('修改成功')
      store.changeUsername( username.value)
      username.value = ''
      password.value = ''
    }catch(err){
      console.log(err)
    }
  }
</script>

<style scoped>
  .form-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 24px;
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  @media (max-width: 960px) {
    .form-container {
      padding: 16px;
    }
  }
  </style>
