// Utilities
import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate' // 新增类型导入
export const useAppStore = defineStore('app',{
  state: () => ({
    isDarkMode: false,
    isLogin: false,
    useId: 0,
  }),
  actions:{
    login (id:number){
      this.isLogin = true
      this.useId = id
    },
    logout (){
      this.isLogin = false
      this.useId = 0
    },
    troggleDarkMode (){
      this.isDarkMode = !this.isDarkMode
    },
  },
  persist:{
    key: 'app-store', // 存储键名
    storage: localStorage, // 使用 localStorage
    paths: ['isLogin', 'useId', 'isDarkMode'], // 只持久化登录状态和用户ID
  } as PersistenceOptions, // 使用类型定义
})
