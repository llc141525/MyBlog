// response

export interface BaseResponse<T> {// 定义统一响应格式
  code: number
  data: T
  message: string
}

export interface ArticleHomeResponse {
  id: number,
  title: string,
  cover_url: string,
  status: boolean,
  createTime: string,
  updateTime: string,
  usersId: number,
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
  ChildComment: ChildComment[],
}

interface ChildComment {
  id: number,
  content: string,
  createTime: string,
  username: string,
  usersAvatarUrl: string,
}


// request

export interface CreateArticleRequest {
  title: string,
  content: string,
  cover_url: string,
  status: boolean,
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
}

export interface UsersRequest {
  username?: string,
  password?: string
}
