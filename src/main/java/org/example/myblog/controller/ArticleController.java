package org.example.myblog.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "文章相关接口")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "新建文章")
    @PostMapping("/create")
    public ApiResponse<Void> create(@Valid @RequestBody CreateArticleRequest request,
                                    @RequestAttribute Long userId) {
        articleService.createArticle(request, userId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "展示主页文章")
    @GetMapping("home/{page}")
    public ApiResponse<List<ArticleHomeResponse>> getAllArticles(@RequestAttribute Long userId,
                                                                 @PathVariable Integer page
    ) {
        List<ArticleHomeResponse> allArticles = articleService.getAllArticles(userId, page);
        return ApiResponse.success(allArticles);
    }

    @Operation(summary = "阅读文章")
    @GetMapping("detail/{articleId}")
    public ApiResponse<ArticleDetailResponse> getArticleDetail(@PathVariable Long articleId) {
        ArticleDetailResponse articleById = articleService.getArticleById(articleId);
        return ApiResponse.success(articleById);
    }

    @Operation(summary = "更新文章")
    @PatchMapping("/")
    public ApiResponse<Void> updateArticle(UpdateArticleRequest request) {
        articleService.updateArticle(request);
        return ApiResponse.success(null);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{articleId}")
    public ApiResponse<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ApiResponse.success(null);
    }
}
