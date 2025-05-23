package org.example.myblog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.service.ArticleService;
import org.example.myblog.service.CommentService;
import org.example.myblog.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    @PostMapping("/")
    public ApiResponse<Void> createComment(@Valid @RequestBody CreateCommentRequest request,
                                           @RequestAttribute Long userId) {
        commentService.createComment(request, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{articleId}")
    public ApiResponse<List<CommentResponse>> getAllCommentByArticle(@PathVariable Long articleId) {
        List<CommentResponse> allCommentByArticle = commentService.getAllCommentByArticle(articleId);
        return ApiResponse.success(allCommentByArticle);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success(null);
    }

}
