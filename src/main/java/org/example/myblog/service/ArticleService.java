package org.example.myblog.service;


import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.request.UpdateArticleRequest;
import org.example.myblog.dto.response.*;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.ArticleError;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.mapper.ArticleMapper;
import org.example.myblog.model.Article;
import org.example.myblog.model.Users;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.CommentRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final CommentService commentService;
    private final RedisCacheManager cacheManager;
    private final CommentRepository commentRepository;
    private final UserService userService;

    // Service层改造
    @Transactional(readOnly = true)
    public PageResponse<ArticleHomeResponse> getAllArticles(Integer page) {
        int size = 6;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articlePage = articleRepository.findAll(pageable); // 改用 Spring Data 分页查询

        List<ArticleHomeResponse> data = articlePage.getContent()
                .stream()
                .map(articleMapper::articleToArticleHomeResponse)
                .toList();

        return new PageResponse<>(
                data,
                page,
                size,
                articlePage.getTotalPages(),
                articlePage.getTotalElements()
        );
    }

    @Transactional
    public Long createArticle(CreateArticleRequest request, Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        Article article = articleMapper.createArticleRequestToArticle(request);
        if (request.cover() != null && !request.cover().isEmpty()) {
            try {
                String path = userService.downloadFile(request.cover());
                article.setCover_url(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        users.addArticle(article);
        Article save = articleRepository.save(article);
        return save.getId();
    }


    @Transactional
    @CacheEvict(value = "article", key = "#request.articleId()")
    public void updateArticle(UpdateArticleRequest request, Long userId) {
        Article article = articleRepository.findById(request.articleId())
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
        Users users = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(UserError.USER_NOT_FOUND));
        if (!article.getUsers().equals(users)) {
            throw new BusinessException(ArticleError.NOT_OWNER);
        }

        // 增量更新, 判断是否为空. 如果为空, 就更新对应的值
        Optional.ofNullable(request.title()).ifPresent(article::setTitle);
        Optional.ofNullable(request.cover_url()).ifPresent(article::setCover_url);
        Optional.ofNullable(request.content()).ifPresent(article::setContent);
        Optional.ofNullable(request.status()).ifPresent(article::setStatus);
        Optional.ofNullable(request.summarize())
                .map(s -> s.length() > 100 ? s.substring(0, 100) : s)
                .ifPresent(article::setSummarize);
    }

    @Transactional
    @CacheEvict(value = "article", key = "#articleId")
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));

        // 删除文章所属的评论
        article.getComments().clear();

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

    public List<OwnerArticleResponse> getOwnerArticle(Long userId) {
        List<Article> byUsersId = articleRepository.findByUsers_Id(userId);
        return byUsersId
                .stream()
                .map(articleMapper::articleToOwnerArticleResponse)
                .collect(Collectors.toList());
    }

    public List<AllArticleResponse> getArticleToSearch() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::articleToAllArticleResponse)
                .toList();
    }
}

