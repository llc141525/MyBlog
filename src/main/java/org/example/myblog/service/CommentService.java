package org.example.myblog.service;

import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.ArticleError;
import org.example.myblog.exception.errors.CommentError;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.mapper.CommentMapper;
import org.example.myblog.model.Article;
import org.example.myblog.model.Comment;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.CommentRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "commentResponse", key = "#request.articleId()")
    public void createComment(CreateCommentRequest request, Long userId) {
        Comment comment = commentMapper.CreateCommentRequestToComment(request);
        // 如果这条新建评论请求的 parentCommentId 不为空, 那么这条评论就是一条用于评论的评论.
        if (request.parentCommentId() != null) {
            commentRepository.findById(request.parentCommentId()).ifPresentOrElse(
                    commentParent -> {
                        // 维护和父级评论的双向关系
                        commentParent.addChildComment(comment);
                    }, () -> {
                        throw new BusinessException(CommentError.COMMENT_NOT_FOUND);
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

    @Cacheable(value = "commentResponse", key = "#articleId")
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllCommentByArticle(Long articleId) {
        Article article = articleRepository.findArticleByIdWithUserComments(articleId)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
        // 返回相应
        List<CommentResponse> responses = new ArrayList<>();
        // 排除列表, 如果出现在这个列表中, 代表他是一个子评论.
        List<Long> excludeCommentIds = new ArrayList<>();

        article.getComments().forEach(comment -> {
            // 如果他是一个子评论, 那么他不该出现在父级评论中
            if (excludeCommentIds.contains(comment.getId())) {
                return;
            }

            List<CommentResponse.CommentDto> childComment = new ArrayList<>();

            comment.getChildComment().forEach(commentChild -> {
                excludeCommentIds.add(commentChild.getId());
                childComment.add(commentMapper.commentToCommentDto(commentChild));
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

    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(CommentError.COMMENT_NOT_FOUND));

        // 如果这条评论有父级评论, 那么接触双向关系
        Optional.ofNullable(comment.getParentComment()).ifPresent(comment::removeChildComment);


        // 解除和 article 的双向关系
        articleRepository.findById(comment.getArticle().getId()).ifPresentOrElse(
                article -> article.removeComment(comment),
                () -> {
                    throw new BusinessException(ArticleError.ARTICLE_NOT_FOUND);
                });

        // 解除和 user 的双向关系
        userRepository.findById(comment.getUsers().getId()).ifPresentOrElse(
                users -> users.removeComment(comment),
                () -> {
                    throw new BusinessException(UserError.USER_NOT_FOUND);
                });

        commentRepository.deleteById(commentId);
    }
}
