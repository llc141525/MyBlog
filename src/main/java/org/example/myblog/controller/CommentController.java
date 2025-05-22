package org.example.myblog.controller;

import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.service.CommentService;
import org.example.myblog.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    public ApiResponse<Void> createComment(@RequestBody CreateCommentRequest request,
                                           @RequestAttribute Long userId) {
        commentService.createComment(request, userId);
        return ApiResponse.success(null);
    }

}
