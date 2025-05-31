import type {
  ArticleDetailResponse,
  ArticleHomeResponse,
  BaseResponse,
  CreateArticleRequest,
  CreateCommentRequest,
  ErrorResponse,
  Pageable,
  UpdateArticleRequest,UsersRequest,

} from '@/types/index.ts'

// 用于请求的默认数据
export class defaultFactory {
  // 用于创建的请求的工厂函数
  static defaultUsers () {
    return {
      username: '',
      password: '',
    } satisfies UsersRequest
  }

  static defaultCreateArticle ():CreateArticleRequest {
    return {
      title: '默认标题',
      content: '默认内容',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
    }
  }

  static defaultCreateComment ():CreateCommentRequest {
    return {
      content: '默认内容',
      articleId: 1,
    }
  }

  static defaultCreateUsers ():UsersRequest {
    return {
      username: 'admin',
      password: 'admin',

    }
  }

  // 用于更新的请求的工厂函数
  static defaultUpdateUser ():UsersRequest {
    return {
      username: '',
      password: '',

    }
  }

  static defaultUpdateArticle () : UpdateArticleRequest{
    return {
      articleId: 1,
      title: '默认标题',
      content: '默认内容',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
    }
  }

  // 默认响应
  static defaultArticleHomeResponse ():ArticleHomeResponse {
    return {
      id: 1,
      title: '默认标题',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
      createTime: '2021-01-01 00:00:00',
      updateTime: '2021-01-01 00:00:00',
      usersId: 1,
      commentLength:0,
      summarize: '默认内容',
    }
  }

  static defaultArticleDetailResponse () :ArticleDetailResponse{
    return {

      id: 1,
      title: '默认标题',
      content: '默认内容',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
      createTime: '2021-01-01 00:00:00',
      updateTime: '2021-01-01 00:00:5.00E01',
      usersId: 1,
      usersAvatarUrl: 'https://picsum.photos/200/300',
      commentIds: [1, 2, 3],

    }
  }


  static defaultBaseResponse (){
    return{
      code: 0,
      data: null,
      message: '',
    }satisfies BaseResponse<unknown>
  }

  static defaultErrorResponse ():ErrorResponse {
    return {
      status: 0,
      code: 0,
      message: '',
      data: null,
    }
  }

  static defaultPageableResponse<T> ()
    :Pageable<T>
  {
    return {
      data: [] as T[],
      totalPages: 1,
      currentPage: 1,
      totalElements: 0,
      pageSize: 0,
    }
  }
}
