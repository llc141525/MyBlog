package org.example.myblog.controller;


import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateArticleRequest;
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
    public ApiResponse<Void> create(@RequestBody CreateArticleRequest request,
                                    @RequestAttribute Long userId) {
        articleService.createArticle(request, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/")
    public ApiResponse<List<ArticleHomeResponse>> getAllArticles(@RequestAttribute Long userId) {
        List<ArticleHomeResponse> allArticles = articleService.getAllArticles(userId);
        return ApiResponse.success(allArticles);
    }
}
