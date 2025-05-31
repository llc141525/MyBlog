<template>
  <VFileInput
    v-model="avatar"
    accept="image/*"
    label="上传图片"
  >
    <template #append>
      <VBtn text="上传头像" @click=" updateAvatar" />
    </template>
  </VFileInput>
</template>
<script setup lang="ts">
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
</script>
