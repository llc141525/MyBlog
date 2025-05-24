package org.example.myblog.service;


import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.request.UpdateArticleRequest;
import org.example.myblog.dto.response.ArticleDetailResponse;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.ArticleError;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.mapper.ArticleMapper;
import org.example.myblog.model.Article;
import org.example.myblog.model.Users;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final CommentService commentService;
    private final RedisCacheManager cacheManager;

    // 按照 userId 缓存文章
    @Transactional(readOnly = true)
    public List<ArticleHomeResponse> getAllArticles(Integer page) {
        // 设置首页展示文章数量为 6
        int size = 6;
        // 页数是从 1 开始的
        page -= 1;
        // 页数必须为正整数.
        if (page < 0)
            throw new BusinessException(ArticleError.PAGE_NOT_FOUND);

        Pageable pageable = PageRequest.of(page, size);

        return articleRepository.findAllByPage(pageable)
                .stream()
                .map(articleMapper::articleToArticleHomeResponse)
                .toList();
    }

    @Transactional
    public void createArticle(CreateArticleRequest request, Long userId) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            Article article = articleMapper.createArticleRequestToArticle(request);
            // 维护双向关系
            user.addArticle(article);
            articleRepository.save(article);
        }, () -> {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        });
    }

    @Transactional
    @CacheEvict(value = "article", key = "#request.articleId()")
    public void updateArticle(UpdateArticleRequest request, Long userId) {
        Article article = articleRepository.findById(request.articleId())
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
        Users users = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(UserError.USER_NOT_FOUND));
        if (article.getUsers().equals(users)) {
            throw new BusinessException(ArticleError.NOT_OWNER);
        }

        // 增量更新, 判断是否为空. 如果为空, 就更新对应的值
        Optional.ofNullable(request.title()).ifPresent(article::setTitle);
        Optional.ofNullable(request.cover_url()).ifPresent(article::setCover_url);
        Optional.ofNullable(request.content()).ifPresent(article::setContent);
        Optional.ofNullable(request.status()).ifPresent(article::setStatus);
    }

    @Transactional
    @CacheEvict(value = "article", key = "#articleId")
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));

        // 删除文章所属的评论
        article.getComments().forEach(comment -> commentService.deleteComment(comment.getId()));

        // 移除和 user 的双向关系
        article.getUsers().removeArticle(article);

        // 在数据库里面删除文章
        articleRepository.delete(article);

        // 清除评论缓存
        Objects.requireNonNull(cacheManager.getCache("comment")).evict(articleId);
    }

    @Cacheable(value = "article", key = "#articleId")
    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleById(Long articleId) {
        return articleRepository
                .findById(articleId)
                .map(articleMapper::articleToArticleDetailResponse)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
    }


}

