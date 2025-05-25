import type { BaseResponse, UsersRequest, UsersResponse } from '@/types'

import request from '@/utils/http'

export const usersApi = {
  // 登录请求
  login (data: UsersRequest) {
    return request.post<UsersResponse>('/user/login', data, {
      headers: { 'Content-Type': 'application/json' },
    })
  },
  register (data: UsersRequest) {
    return request.post<UsersResponse>('/user/register', data)
  },
  logout () {
    return request.get<BaseResponse<null>>('/user/logout')
  },
  update (data: UsersRequest) {
    return request.patch('/user/', data)
  },
}
