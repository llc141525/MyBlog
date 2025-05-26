import type { BaseResponse, CommentResponse, CreateCommentRequest } from '@/types'
import request from '@/utils/http'


export const commentApi = {
  getComment (articleId: number) {
    return request.get<CommentResponse[]>('/comment/get', { params: { articleId } })
  },
  createComment (data: CreateCommentRequest) {
    return request.post<CommentResponse>('/comment/', data)
  },
  deleteComment (commentId: number) {
    return request.delete<BaseResponse<null>>('/comment/', {
      params: { commentId },
    })
  },
}
