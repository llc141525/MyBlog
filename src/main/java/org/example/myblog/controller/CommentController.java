package org.example.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.service.CommentService;
import org.example.myblog.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "评论相关接口")
class CommentController {
    private final CommentService commentService;

    @Operation(summary = "新建评论")
    @PostMapping("/")
    public ApiResponse<Void> createComment(@Valid @RequestBody CreateCommentRequest request,
                                           @RequestAttribute Long userId) {
        commentService.createComment(request, userId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "获取评论", description = "获取一篇文章的所有评论, 返回一个二级的评论树")
    @GetMapping("/get/{articleId}")
    public ApiResponse<List<CommentResponse>> getAllCommentByArticle(@PathVariable Long articleId) {
        List<CommentResponse> allCommentByArticle = commentService.getAllCommentByArticle(articleId);
        return ApiResponse.success(allCommentByArticle);
    }

    @Operation(summary = "删除评论", description = "删除一个评论, 可以删除父级评论, 如果删除父级评论, 那么会同时删除所有子级评论 ")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success(null);
    }

}
