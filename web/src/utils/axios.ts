// src/utils/request.ts
import type { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import axios from 'axios'
import router from '../router'
import type { RouteLocationRaw } from 'vue-router'

// 定义响应数据的基础结构
interface ApiResponse<T = never> {
  code: number
  message?: string
  data: T
}

// 扩展自定义请求配置
interface CustomRequestConfig extends AxiosRequestConfig {
  fileUpload?: boolean
  responseType?: 'json' | 'blob' | 'arraybuffer' | 'document' | 'text' | 'stream'
}

// 扩展 AxiosRequestConfig 类型
declare module 'axios' {
  export interface AxiosRequestConfig {
    fileUpload?: boolean
  }
}

// 创建带有自定义配置的 Axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API as string,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
    const token = localStorage.getItem('my_blog_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    if (config.fileUpload) {
      config.headers['Content-Type'] = 'multipart/form-data'
    }

    return config
  },
  (error: AxiosError) => Promise.reject(error)
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse): Promise<any> => {
    if (response.config.responseType === 'blob') {
      return Promise.resolve(response.data)
    }

    const res = response.data as ApiResponse
    if (res.code !== 200) {
      handleError(res.message || 'Error', { response: res })
      return Promise.reject(new Error(res.message || 'Error'))
    }

    return Promise.resolve(res.data)
  },
  (error: AxiosError<ApiResponse>) => {
    const errorMessage = error.response?.data?.message ||
            error.message ||
            'Network Error'
    handleError(errorMessage, error)
    return Promise.reject(error)
  }
)

// 错误处理函数
function handleError (message: string, error: any): void {
  // 这里替换为实际的 UI 提示库调用，例如 Vuetify 的 snackbar
  console.error(message)

  if (error?.response?.status === 401) {
    router.push('/login' as RouteLocationRaw)
  }
}

// 核心请求函数
function request<T = any> (options: CustomRequestConfig): Promise<T> {
  return service({
    method: options.method || 'get',
    url: options.url,
    [options.method === 'get' ? 'params' : 'data']: options.data,
    ...options,
  }).then(response => response as T)
}

// 封装常用方法
const http = {
  get<T = any>(url: string, params?: Record<string, any>, config?: CustomRequestConfig): Promise<T> {
    return request<T>({ method: 'get', url, params, ...config })
  },

  post<T = any>(url: string, data?: any, config?: CustomRequestConfig): Promise<T> {
    return request<T>({ method: 'post', url, data, ...config })
  },

  put<T = any>(url: string, data?: any, config?: CustomRequestConfig): Promise<T> {
    return request<T>({ method: 'put', url, data, ...config })
  },

  delete<T = any>(url: string, data?: any, config?: CustomRequestConfig): Promise<T> {
    return request<T>({ method: 'delete', url, data, ...config })
  },

  upload<T = any>(url: string, file: File): Promise<T> {
    const formData = new FormData()
    formData.append('file', file)
    return this.post<T>(url, formData, { fileUpload: true })
  },
}

export default http
