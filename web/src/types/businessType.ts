// response

export interface BaseResponse<T> {// 定义统一响应格式
  code: number
  data: T
  message: string
}

export interface ErrorResponse{
  status: number,
  code : number,
  message: string
  data: unknown
}

export interface Pageable<T> {
  data: T[],
  totalPages: number,
  currentPage: number,
  totalElements: number,
  pageSize: number
}

export interface ArticleHomeResponse {
  id: number,
  title: string,
  cover_url: string,
  status: boolean,
  createTime: string,
  updateTime: string,
  usersId: number,
  commentLength: number,
  summarize: string,
  authorName: string
}

export interface ArticleDetailResponse {
  id: number,
  title: string,
  content: string,
  cover_url: string,
  status: boolean,
  createTime: string,
  updateTime: string,
  usersId: number,
  usersAvatarUrl: string,
  commentIds: number[],
  summarize: string
}

export interface UsersResponse {
  id: number,
  username: string,
  avatarUrl: string,
}

export interface CommentResponse {
  id: number,
  content: string,
  createTime: string,
  updateTime: string,
  usersId: number,
  username: string,
  usersAvatarUrl: string,
  childComment: ChildComment[],
}

interface ChildComment {
  id: number,
  content: string,
  createTime: string,
  username: string,
  avatar: string,
}


// request

export interface CreateArticleRequest {
  title: string,
  content: string,
  cover_url: string,
  status: boolean,
  cover: File | null,
  summarize?: string
}

export interface CreateCommentRequest {
  content: string,
  articleId: number,
  parentCommentId?: number | null,
}

export interface UpdateArticleRequest {
  articleId: number,
  title?: string,
  content?: string,
  cover_url?: string,
  status?: boolean,
  summarize?: string
}

export interface UsersRequest {
  username?: string,
  password?: string
}
