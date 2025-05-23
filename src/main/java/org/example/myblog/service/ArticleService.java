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
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    @Transactional
    @CacheEvict(value = {"articleHome", "articleDetail"}, allEntries = true)
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

    // 按照 userId 缓存文章
    @Transactional(readOnly = true)
    @Cacheable(value = "articleHome", key = "#page")
    public List<ArticleHomeResponse> getAllArticles(Long userId, Integer page) {
        int size = 6;
        // 页数是从 1 开始的
        page -= 1;
        // 页数必须为正整数.
        if (page <= 0)
            throw new BusinessException(ArticleError.PAGE_NOT_FOUND);

        Pageable pageable = PageRequest.of(page, size);

        return articleRepository.findArticlesByUserId(userId, pageable)
                .stream()
                .map(articleMapper::articleToArticleHomeResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"articleHome", "articleDetail"}, allEntries = true)
    public void updateArticle(UpdateArticleRequest request) {
        Article article = articleRepository.findById(request.id())
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));

        // 增量更新, 判断是否为空. 如果为空, 就更新对应的值
        Optional.ofNullable(request.title()).ifPresent(article::setTitle);
        Optional.ofNullable(request.cover_url()).ifPresent(article::setCover_url);
        Optional.ofNullable(request.content()).ifPresent(article::setContent);
        Optional.ofNullable(request.status()).ifPresent(article::setStatus);
    }


    @Cacheable(value = "articleDetail", key = "#articleId")
    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleById(Long articleId) {
        return articleRepository
                .findById(articleId)
                .map(articleMapper::articleToArticleDetailResponse)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
    }

    @Transactional
    @CacheEvict(value = {"articleHome", "articleDetail"}, allEntries = true)
    public void deleteArticle(Long articleId) {
        if (articleRepository.existsById(articleId)) {
            articleRepository.deleteById(articleId);
        }
    }
}

