// Utilities
import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate' // 新增类型导入
export const useAppStore = defineStore('app',{
  state: () => ({
    isDarkMode: false,
    isLogin: false,
    useId: 0,
    username: '',
    avatarUrl: '',
  }),
  actions:{
    login (id:number, username:string, avatarUrl:string){
      this.isLogin = true
      this.useId = id
      this.username = username
      this.avatarUrl = avatarUrl
    },
    logout (){
      this.isLogin = false
      this.useId = 0
      this.username = ''
      this.avatarUrl = ''
    },
    troggleDarkMode (){
      this.isDarkMode = !this.isDarkMode
    },
    changeAvatar (avatarUrl:string){
      this.avatarUrl = avatarUrl
    },
    changeUsername( username:string){
      this.username = username
    }
  },
  persist:{
    key: 'app-store', // 存储键名
    storage: localStorage, // 使用 localStorage
    paths: ['isLogin', 'useId', 'isDarkMode', 'username', 'avatarUrl'], // 只持久化登录状态和用户ID
  } as PersistenceOptions, // 使用类型定义
})
