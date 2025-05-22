package org.example.myblog.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.request.UpdateArticleRequest;
import org.example.myblog.dto.response.ArticleDetailResponse;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.service.ArticleService;
import org.example.myblog.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public ApiResponse<Void> create(@Valid @RequestBody CreateArticleRequest request,
                                    @RequestAttribute Long userId) {
        articleService.createArticle(request, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{page}/{size}")
    public ApiResponse<List<ArticleHomeResponse>> getAllArticles(@RequestAttribute Long userId,
                                                                 @PathVariable Integer page,
                                                                 @PathVariable Integer size) {
        List<ArticleHomeResponse> allArticles = articleService.getAllArticles(userId, page, size);
        return ApiResponse.success(allArticles);
    }

    @GetMapping("/{articleId}")
    public ApiResponse<ArticleDetailResponse> getArticleDetail(@PathVariable Long articleId) {
        ArticleDetailResponse articleById = articleService.getArticleById(articleId);
        return ApiResponse.success(articleById);
    }

    @PatchMapping("/")
    public ApiResponse<Void> updateArticle(UpdateArticleRequest request) {
        articleService.updateArticle(request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{articleId}")
    public ApiResponse<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ApiResponse.success(null);
    }
}
