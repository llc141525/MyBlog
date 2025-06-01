<template>
  <div class="text-center">
    <v-menu
      v-model="menu"
      :close-on-content-click="false"
      location="end"
    >

      <template #activator="{ props }">
        <v-avatar>
          <v-img
            alt="作者"
            :src="store.avatarUrl || 'https://picsum.photos/500/300?random=1'"
            v-bind="props"
          />
        </v-avatar>
      </template>

      <v-card min-width="300">
        <v-list>
          <v-list-item
            :prepend-avatar="store.avatarUrl || 'https://picsum.photos/500/300?random=1'"
            subtitle="邮箱"
            :title="store.username"
          >
            <template #append>
              <v-btn
                :class="fav ? 'text-red' : ''"
                icon="mdi-heart"
                variant="text"
                @click="fav = !fav"
              />
            </template>
          </v-list-item>
        </v-list>

        <v-divider />

        <v-list>
          <v-list-item>
            <v-switch
              v-model="message"
              color="purple"
              hide-details
              label="Enable messages"
            />
          </v-list-item>

          <v-list-item>
            <v-switch
              v-model="hints"
              color="purple"
              hide-details
              label="Enable hints"
            />
          </v-list-item>
        </v-list>

        <v-card-actions>
          <v-spacer />

          <v-btn
            variant="text"
            @click="menu = false"
          >
            关闭
          </v-btn>
          <VBtn
            text="注册"
            @click="register"
          />
          <v-btn
            color="primary"
            variant="text"
            @click="logout"
          >
            登出
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>
  </div>
</template>

<script setup lang="ts">
  import { usersApi } from '@/api/users';
  import { useAppStore } from '@/stores/app';
  import { VBtn } from 'vuetify/components';
  const menu = ref(false);
  const store = useAppStore()
  const fav = ref(false);
  const message = ref(true);
  const hints = ref(false);
  const router = useRouter()

  const logout = async ()=>{
    try {
      await usersApi.logout();
      menu.value = false;
      store.userId = 0
      store.isLogin = false
      router.push('/auth/login')
    }catch(err){
      console.warn(err)
    }
  }

  const register = ()=>{
    router.push('/auth/register')
    menu.value = false;
  }
</script>
