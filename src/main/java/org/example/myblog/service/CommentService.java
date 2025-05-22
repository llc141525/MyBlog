package org.example.myblog.service;

import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.ArticleError;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.mapper.CommentMapper;
import org.example.myblog.model.Comment;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.CommentRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(CreateCommentRequest request, Long userId) {
        Comment comment = commentMapper.CreateCommentRequestToComment(request);
        // 如果这条新建评论请求的 parentCommentId 不为空, 那么这条评论就是一条用于评论的评论.
        if (request.parentCommentId() != null) {
            commentRepository.findById(request.parentCommentId()).ifPresent(
                    commentParent -> {
                        commentParent.addChildComment(comment);
                    });
        }
        // 维护和文章的双向关系
        articleRepository.findById(request.articleId()).ifPresentOrElse(
                article -> {
                    article.addComment(comment);
                },
                () -> {
                    throw new BusinessException(ArticleError.ARTICLE_NOT_FOUND);
                }
        );

        // 维护和用户的双向关系
        userRepository.findById(userId).ifPresentOrElse(users -> {
            users.addComment(comment);
        }, () -> {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        });

        commentRepository.save(comment);
    }
}
