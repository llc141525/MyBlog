// src/global.d.ts
import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  BaseResponse,
  CommentResponse,
  CreateArticleRequest,
  CreateCommentRequest,
  ErrorResponse,
  Pageable,
  UpdateArticleRequest,
  UsersRequest,
  UsersResponse,

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

// 声明工厂函数

declare module '@/types/factory' {
  // 声明工厂函数的类型

  // 声名请求工厂函数
  export function defaultUsers (): UsersRequest;
  export function defaultCreateArticle (): CreateArticleRequest;
  export function defaultCreateComment (): CreateCommentRequest;
  export function defaultCreateUsers (): UsersRequest;
  export function defaultUpdateUser (): UsersRequest;
  export function defaultUpdateArticle (): UpdateArticleRequest;

  // 声名响应的工厂函数
  export function defaultArticleHomeResponse (): ArticleHomeResponse;
  export function defaultArticleDetailResponse (): ArticleDetailResponse;
  export function defaultUsersResponse (): UsersResponse;
  export function defaultCommentResponse (): CommentResponse;
  export function defaultBaseResponse<T> (): BaseResponse<T>;
  export function defaultErrorResponse (): ErrorResponse;
  export function defaultPageableResponse<T> (): Pageable<T>;


}

export {};
