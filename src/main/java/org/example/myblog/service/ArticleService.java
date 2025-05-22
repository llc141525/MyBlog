package org.example.myblog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.model.Article;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    @Transactional
    @CacheEvict(value = "article", key = "#userId")
    public void createArticle(CreateArticleRequest request, Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            Article article = Article.builder()
                    .content(request.content())
                    .title(request.title())
                    .status(request.status())
                    .cover_url(request.cover_url())
                    .build();
            // 维护双向关系
            user.addArticle(article);
            articleRepository.save(article);
        });
    }

    // 按照 userId 缓存文章
    @Cacheable(value = "article", key = "#userId", unless = "#result.isEmpty()")
    public List<ArticleHomeResponse> getAllArticles(Long userId) {

        List<ArticleHomeResponse> responses = new ArrayList<>();
        articleRepository.findArticlesByUserId(userId).forEach(article -> {
            ArticleHomeResponse build = ArticleHomeResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .cover_url(article.getCover_url())
                    .updateTime(article.getUpdateTime())
                    .createTime(article.getCreateTime())
                    .status(article.getStatus())
                    .userId(article.getUsers().getId())
                    .build();
            responses.add(build);
        });
        return responses;
    }
}

