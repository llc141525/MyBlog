import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  BaseResponse,
  CreateArticleRequest,
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
  createArticle (data: CreateArticleRequest) {
    return request.post<ArticleDetailResponse>('/article/create', data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
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
