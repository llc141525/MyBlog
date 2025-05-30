// src/global.d.ts
import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  UsersResponse,
  CommentResponse,
  CreateArticleRequest,
  ErrorResponse,
  BaseResponse,
  Pageable,
  CreateCommentRequest,
  UpdateArticleRequest,
  UsersRequest

  // 导入其他常用类型...
} from '@/types/businessType';

declare global {
    // 声明全局类型别名
    type ArticleHomeRes = ArticleHomeResponse;
    type ArticleDetailRes = ArticleDetailResponse;
    type UsersRes = UsersResponse;
    type CommentRes = CommentResponse;
    type CreateArticleReq = CreateArticleRequest;
    type CreateCommentReq = CreateCommentRequest;
    type UpdateArticleReq = UpdateArticleRequest;
    type UsersReq = UsersRequest;
    type ErrorRes = ErrorResponse;
    type BaseRes<T> = BaseResponse<T>;
    type PageableRes<T> = Pageable<T>;
    
    

}
export {};
