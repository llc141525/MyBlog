import type { BaseResponse, ErrorResponse, UsersRequest , UsersResponse } from '@/types'

import request from '@/utils/http'

export const usersApi = {
  // 登录请求
  async login (data: UsersRequest) {
    return await request.post<UsersResponse>('/user/login', data, {
      headers: { 'Content-Type': 'application/json' },
    })
  },
  async register (data: UsersRequest) {
    try{
      return await request.post<UsersResponse>('/user/register', data)
    }catch(err ){
      console.warn('注册失败' , err)
      throw new Error((err as ErrorResponse).message)
    }
  },
  logout () {
    return request.get<BaseResponse<null>>('/user/logout')
  },
  update (data: UsersRequest) {
    return request.patch('/user/', data)
  },
}
