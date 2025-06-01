import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  BaseResponse,
  CreateArticleRequest,
  ErrorResponse,
  Pageable,
  UpdateArticleRequest,
} from '@/types'
import request from '@/utils/http'

export const articleApi = {
  getArticleHome (page: number) {
    return request.get<Pageable<ArticleHomeResponse>>('/article/home', {
      params: { page },
    })
  },
  getArticleDetail (articleId: number) {
    return request.get<ArticleDetailResponse>('/article/detail', {
      params: { articleId },
    })
  },
  async createArticle (data: CreateArticleRequest) {
    try{
      return request.post<ArticleDetailResponse>('/article/create', data, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
    }catch(err){
      console.log('文章创建失败')
      throw new Error((err as ErrorResponse).message)
    }
  },

  updateArticle (data: UpdateArticleRequest) {
    return request.patch<ArticleDetailResponse>('/article/', data)
  },
  deleteArticle (articleId: number) {
    return request.delete<BaseResponse<null>>('/article/', {
      params: { articleId },
    })
  },
}
