<template>
  <div>

    <v-container
      class="form-container"
      fluid
      max-width="1200"
    >
      <v-card
        class="mb-6"
        elevation="2"
      >
        <v-card-title class="text-h6 font-weight-medium pa-6">
          头像设置
        </v-card-title>

        <v-divider />

        <v-card-text class="pa-6">
          <v-row
            align="center"
            class="g-4"
          >
            <v-col
              cols="12"
              md="8"
            >
              <v-file-input
                v-model="avatar"
                accept="image/*"
                class="mb-2"
                clearable
                density="comfortable"
                hide-details="auto"
                label="上传头像"
                outlined
                prepend-icon="mdi-camera"
              />
            </v-col>

            <v-col
              class="text-md-right"
              cols="12"
              md="4"
            >
              <v-btn
                block
                class="mt-md-0 mt-2"
                color="primary"
                :disabled="!avatar"
                size="large"
                @click="updateAvatar"
              >
                <v-icon left>mdi-upload</v-icon>
                更新头像
              </v-btn>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>

      <v-card
        class="mb-6"
        elevation="2"
      >
        <v-card-title class="text-h6 font-weight-medium pa-6">
          个人信息
        </v-card-title>

        <v-divider />

        <v-card-text class="pa-6">
          <v-form @submit.prevent="updateUser">
            <v-row class="g-4">
              <v-col cols="12">
                <v-text-field
                  v-model="username"
                  class="mb-2"
                  clearable
                  density="comfortable"
                  hide-details="auto"
                  label="用户名"
                  outlined
                  prepend-icon="mdi-account"
                />
              </v-col>

              <v-col cols="12">
                <v-text-field
                  v-model="password"
                  class="mb-4"
                  clearable
                  density="comfortable"
                  hide-details="auto"
                  label="密码"
                  outlined
                  prepend-icon="mdi-lock"
                  type="password"
                />
              </v-col>

              <v-col
                class="text-right pt-4"
                cols="12"
              >
                <v-btn
                  class="px-8"
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
        </v-card-text>
      </v-card>

      <v-card elevation="2">
        <v-card-title class="text-h6 font-weight-medium pa-6">
          我的文章
        </v-card-title>

        <v-divider />

        <v-card-text class="pa-0">
          <v-list class="py-0">
            <template
              v-for="item in owner"
              :key="item.articleId"
            >
              <v-list-item class="px-6 py-5">
                <v-row
                  align="center"
                  class="g-4"
                >
                  <v-col
                    cols="12"
                    md="8"
                  >
                    <div class="article-info">
                      <h3 class="text-h6 font-weight-medium mb-2">
                        {{ item.title }}
                        <v-chip
                          v-if="!item.status"
                        >暂存</v-chip>

                      </h3>
                      <p class="text-body-1 text-medium-emphasis mb-0">
                        {{ item.summarize }}
                      </p>
                    </div>
                  </v-col>

                  <v-col
                    class="text-md-right"
                    cols="12"
                    md="4"
                  >

                    <v-btn
                      append-icon="mdi-eye"
                      class="ma-2"
                      color="primary"
                      text="查看"
                      @click="toRead(item.articleId)"
                    />

                    <v-btn
                      append-icon="mdi-pencil"
                      class="ma-2"
                      color="secondary"
                      text="修改"
                      @click="toChange(item.articleId)"
                    />

                    <v-btn
                      append-icon="mdi-delete"
                      class="ma-2"
                      color="error"
                      text="删除"
                      @click="toDel( item.articleId)"
                    />

                  </v-col>
                </v-row>
              </v-list-item>
              <v-divider />
            </template>
          </v-list>
        </v-card-text>
      </v-card>
    </v-container>
    <v-dialog
      v-model="confirmDelete"
      width="auto"
    >
      <v-card
        max-width="400"
        prepend-icon="mdi-update"
        text="你确定删除这篇文章吗, 如果删除将无法找回"
        title="确认删除文章"
      >
        <template #actions>
          <v-btn
            class="ms-auto"
            text="取消"
            @click="confirmDelete = false"
          />
          <v-btn text="确认删除" @click="deletArticle(deleteArticleId)" />
        </template>
      </v-card>
    </v-dialog>
  </div>

</template>


  <script setup lang="ts">
  import { articleApi } from '@/api/article';
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

  type ownerType = {
    articleId: number,
    title: string ,
    summarize : string,
    status: boolean
  }

  const owner = ref<ownerType[]>()
  const getOwner = async ()=>{
    try{
      const res = await articleApi.getOwnerArticle() as ownerType[]
      owner.value = res
      console.log(owner.value)
    }catch(err){
      console.log(err)
    }
  }

  const router = useRouter()
  const toRead = (articleId : number)=>{
    router.push(`article/${articleId}`)
  }
  const confirmDelete = ref(false)
  const deleteArticleId = ref(0)
  const toDel = (id : number)=>{
    deleteArticleId.value = id
    confirmDelete.value = true
  }
  const deletArticle = async (articleId : number)=>{
    try{
      await articleApi.deleteArticle(articleId)
      confirmDelete.value = false
      getOwner()
    }catch (e){
      console.log(e)
    }
  }

  const toChange = (articleId : number)=>{
    router.push(`article/change/${articleId}`)
  }
  onMounted(()=>{
    getOwner()
  })
</script>
<style scoped>
.form-container {
  padding: 24px;
}


/* 列表项悬停效果 */
.v-list-item:hover {
  background-color: rgba(0, 0, 0, 0.04);
}
</style>
