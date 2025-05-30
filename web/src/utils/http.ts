import type { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import axios from 'axios'
import type { BaseResponse } from '@/types'


// 创建 axios 实例
const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

// 定义拦截器
http.interceptors.response.use(
  <T>(response: AxiosResponse<BaseResponse<T>>) => {
    if (response.data.code !== 200 && response.data.code !== 201) {
      return Promise.reject(response.data)// 返回  BaseResponse<T>
    }
    return response.data.data as T
  },
  // 错误处理
  (error: AxiosError<BaseResponse<unknown>>) => {
    if(error.response?.status == 401){
      window.location.href = '/auth/login'
    }

    return Promise.reject({
      status: error.response?.status,
      code: error.response?.data.code,
      message: error.response?.data.message,
      data: error.response?.data.data,
    })
  }
)
// 统一请求
const request = {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return http.get<BaseResponse<T>>(url, config)
      .then(res => res as T)
  },
  post<T, D = unknown>(url: string, data?: D, config?: AxiosRequestConfig) {
    return http.post<BaseResponse<T>, AxiosResponse<BaseResponse<T>>, D>(url, data, config)
      .then(res => res as T)
  },
  put<T, D = unknown>(url: string, data?: D, config?: AxiosRequestConfig) {
    return http.put<BaseResponse<T>, AxiosResponse<BaseResponse<T>>, D>(url, data, config)
      .then(res => res as T)
  },
  patch<T, D = unknown>(url: string, data?: D, config?: AxiosRequestConfig) {
    return http.patch<BaseResponse<T>, AxiosResponse<BaseResponse<T>>, D>(url, data, config)
      .then(res => res as T)
  },
  delete<T>(url: string, config?: AxiosRequestConfig) {
    return http.delete<BaseResponse<T>>(url, config)
      .then(res => res as T)
  },
}

export default request
