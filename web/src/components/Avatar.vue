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
            src="https://cdn.vuetifyjs.com/images/john.jpg"
            v-bind="props"
          />
        </v-avatar>
      </template>

      <v-card min-width="300">
        <v-list>
          <v-list-item
            prepend-avatar="https://cdn.vuetifyjs.com/images/john.jpg"
            subtitle="邮箱"
            title="作者"
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
          <v-btn
            color="primary"
            variant="text"
            @click="menu = false"
          >
            登出
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>
  </div>
</template>

<script>
  export default {
    data: () => ({
      fav: true,
      menu: false,
      message: false,
      hints: true,
    }),
  }
</script>
