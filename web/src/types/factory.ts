import type {
  ArticleHomeResponse,
  CreateArticleRequest,
  CreateCommentRequest,
  UpdateArticleRequest,
  UsersRequest,
} from '@/types/index.ts'

// 用于请求的默认数据
export class defaultFactory {
  static createDefaultArticle (defaults: Partial<CreateArticleRequest>) {
    return {
      title: '默认标题',
      content: '默认内容',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
      ...defaults,
    } satisfies CreateArticleRequest
  }

  static createDefaultComment (defaults: Partial<CreateCommentRequest>) {
    return {
      content: '默认内容',
      articleId: 1,
      ...defaults,
    } satisfies CreateCommentRequest
  }

  static createDefaultUsers (defaults: Partial<UsersRequest>) {
    return {
      username: 'admin',
      password: 'admin',
      ...defaults,
    } satisfies UsersRequest
  }

  static defaultUpdateUser (defaults: Partial<UsersRequest>) {
    return {
      username: 'admin',
      password: 'admin',
      ...defaults,
    } satisfies UsersRequest
  }

  static defaultUsers () {
    return {
      username: 'admin',
      password: 'admin',
    } satisfies UsersRequest
  }

  static defaultUpdateArticle (defaults: Partial<UpdateArticleRequest>) {
    return {
      articleId: 1,
      title: '默认标题',
      content: '默认内容',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
      ...defaults,
    } satisfies UpdateArticleRequest
  }

  static defaultArticleHomeRes (defaults: Partial<ArticleHomeResponse>) {
    return {
      id: 1,
      title: '默认标题',
      cover_url: 'https://picsum.photos/200/300',
      status: true,
      createTime: '2021-01-01 00:00:00',
      updateTime: '2021-01-01 00:00:00',
      usersId: 1,
      ...defaults,
    } satisfies ArticleHomeResponse

  }

}
