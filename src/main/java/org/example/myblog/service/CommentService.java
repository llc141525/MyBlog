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
import org.example.myblog.model.Users;
import org.example.myblog.repository.ArticleRepository;
import org.example.myblog.repository.CommentRepository;
import org.example.myblog.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final RedisCacheManager cacheManager;

    @Transactional
    @CacheEvict(value = {"comment", "article"}, key = "#request.articleId()")
    public void createComment(CreateCommentRequest request, Long userId) {
        Comment comment = commentMapper.CreateCommentRequestToComment(request);
        // 如果这条新建评论请求的 parentCommentId 不为空, 那么这条评论就是一条用于评论的评论.
        if (request.parentCommentId() != null) {
            // 维护和父级评论的双向关系
            commentRepository.findById(request.parentCommentId()).ifPresentOrElse(
                    commentParent -> {
                        // 如果评论树超过两层
                        if (commentParent.getParentComment() != null) {
                            throw new BusinessException(CommentError.ERROR_COMMENT_THREE);
                        }
                        commentParent.addChildComment(comment);
                    },
                    () -> {
                        throw new BusinessException(CommentError.COMMENT_NOT_FOUND);
                    });
        }
        // 维护和文章的双向关系
        articleRepository.findById(request.articleId()).ifPresentOrElse(
                article -> article.addComment(comment),
                () -> {
                    throw new BusinessException(ArticleError.ARTICLE_NOT_FOUND);
                }
        );

        // 维护和用户的双向关系
        userRepository.findById(userId).ifPresentOrElse(
                users -> users.addComment(comment),
                () -> {
                    throw new BusinessException(UserError.USER_NOT_FOUND);
                });
        commentRepository.save(comment);
    }

    @Cacheable(value = "comment", key = "#articleId")
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllCommentByArticle(Long articleId) {
        Article article = articleRepository.findArticleById(articleId)
                .orElseThrow(() -> new BusinessException(ArticleError.ARTICLE_NOT_FOUND));
        // return 的值
        List<CommentResponse> responses = new ArrayList<>();
        // 排除 set , 如果出现在这个 set 中, 代表他是一个子评论.
        Set<Long> excludeCommentIds = new HashSet<>();
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
                    .username(comment.getUsers().getUsername())
                    .usersAvatarUrl(comment.getUsers().getAvatarUrl())
                    .build());
        });
        return responses;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(CommentError.COMMENT_NOT_FOUND));

        // 如果这条评论有父级评论, 那么解除双向关系 --> 删除子评论的情况
        Optional.ofNullable(comment.getParentComment())
                .ifPresent(parentComment -> {
                    parentComment.removeChildComment(comment);
                    // 移除和 article, user 的双向关系
                    removeCommentRelationship(comment);
                });

        // 如果这条评论有子评论, 那么移除和子评论的双向关系 --> 删除父评论的情况
        Optional.ofNullable(comment.getChildComment())
                .ifPresent(childComments ->
                {
                    new ArrayList<>(childComments).forEach(childComment -> {
                        comment.removeChildComment(childComment);
                        removeCommentRelationship(childComment);
                    });
                });

        commentRepository.deleteById(commentId);

        // 清除缓存
        Long articleId = comment.getArticle().getId();

        Objects.requireNonNull(cacheManager.getCache("comment")).evict(articleId);
        Objects.requireNonNull(cacheManager.getCache("article")).evict(articleId);
    }

    @Transactional
    protected void removeCommentRelationship(Comment comment) {
        // 解除和 article 的双向关系
        Article article = comment.getArticle();
        if (article != null) {
            article.removeComment(comment);
        }

        // 解除和 user 的双向关系
        Users users = comment.getUsers();
        if (users != null) {
            users.removeComment(comment);
        }
    }
}
