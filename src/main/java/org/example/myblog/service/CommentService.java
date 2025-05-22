package org.example.myblog.service;

import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.ArticleError;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.mapper.CommentMapper;
import org.example.myblog.model.Article;
import org.example.myblog.model.Comment;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.CommentRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                        // 维护和父级评论的双向关系
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

    public List<CommentResponse> getAllCommentByArticle(Long articleId) {
        // TODO 获取所有评论还有 bug 现在显示是空的
        Article article = articleRepository.findArticleByIdWithUserComments(articleId)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));

        List<CommentResponse> responses = new ArrayList<>();
        article.getComments().forEach(comment -> {
            List<CommentResponse.CommentDto> childComment = new ArrayList<>();

            comment.getChildComment().forEach(commentChild -> {
                childComment.add(CommentResponse.CommentDto.builder()
                        .id(commentChild.getId())
                        .content(commentChild.getContent())
                        .createTime(commentChild.getCreateTime())
                        .build());
            });

            responses.add(CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createTime(comment.getCreateTime())
                    .usersId(comment.getUsers().getId())
                    .usersUsername(comment.getUsers().getUsername())
                    .childComment(childComment)
                    .build());
        });
        return responses;
    }
}
