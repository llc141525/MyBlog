package org.example.myblog.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.request.UpdateArticleRequest;
import org.example.myblog.dto.response.*;
import org.example.myblog.service.ArticleService;
import org.example.myblog.utils.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Tag(name = "文章相关接口")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "新建文章")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> create(@Valid CreateArticleRequest request,
                                    @RequestAttribute Long userId,
                                    HttpServletResponse response) {
        Long article = articleService.createArticle(request, userId);
        response.setHeader("Location", "/article/detail?articleId=" + article);
        return ApiResponse.created(null);
    }

    @Operation(summary = "展示主页文章")
    @GetMapping("home")
    public ApiResponse<PageResponse<ArticleHomeResponse>>
    getAllArticles(@RequestParam Integer page) {
        PageResponse<ArticleHomeResponse> allArticles = articleService.getAllArticles(page);
        return ApiResponse.success(allArticles);
    }

    @Operation(summary = "阅读文章")
    @GetMapping("detail")
    public ApiResponse<ArticleDetailResponse> getArticleDetail(@RequestParam Long articleId) {
        ArticleDetailResponse articleById = articleService.getArticleById(articleId);
        return ApiResponse.success(articleById);
    }

    @Operation(summary = "更新文章")
    @PatchMapping("/")
    public ApiResponse<Void> updateArticle(@RequestBody UpdateArticleRequest request, @RequestAttribute Long userId) {
        articleService.updateArticle(request, userId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/")
    public ApiResponse<Void> deleteArticle(@RequestParam Long articleId) {
        articleService.deleteArticle(articleId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "展示作者作品")
    @GetMapping("/owner-article")
    public ApiResponse<List<OwnerArticleResponse>> getOwnerArticle(@RequestAttribute Long userId) {
        List<OwnerArticleResponse> ownerArticle = articleService.getOwnerArticle(userId);
        return ApiResponse.success(ownerArticle);
    }

    @Operation(summary = "用于搜索")
    @GetMapping("/all-article")
    public ApiResponse<List<AllArticleResponse>> getAllArticle() {
        List<AllArticleResponse> articleToSearch = articleService.getArticleToSearch();
        return ApiResponse.success(articleToSearch);
    }
}
