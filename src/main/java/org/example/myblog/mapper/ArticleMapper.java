package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.response.ArticleDetailResponse;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.model.Article;
import org.example.myblog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "usersId", source = "users.id")
    @Mapping(target = "commentLength", source = "comments")
    @Mapping(target = "summarize", expression = "java(mapSummarize(article))")
    @Mapping(target = "authorName", expression = "java(article.getUsers().getUsername())")
    ArticleHomeResponse articleToArticleHomeResponse(Article article);

    //    @Named("mapSummarize")
    default String mapSummarize(Article article) {
        if (article.getSummarize() != null && !article.getSummarize().isEmpty()) {
            return article.getSummarize();
        } else {
            // 安全处理：当内容不足30字符时
            String content = article.getContent();
            return content != null && content.length() > 100
                    ? content.substring(0, 100)
                    : content; // 直接返回内容（可能为null或短文本）
        }
    }

    default Long articleToCommentLength(List<Comment> comments) {
        return comments.stream().count();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Article createArticleRequestToArticle(CreateArticleRequest createArticleRequest);

    @Mapping(target = "commentIds", source = "comments")
    @Mapping(target = "usersAvatarUrl", source = "users.avatarUrl")
    @Mapping(target = "usersId", source = "users.id")
    ArticleDetailResponse articleToArticleDetailResponse(Article article);

    default List<Long> mapComments(List<Comment> comments) {
        return Optional.ofNullable(comments).orElse(Collections.emptyList())
                .stream().map(Comment::getId).collect(Collectors.toList());
    }

//    default Long userToId(Users user) {
//        return (user != null) ? user.getId() : null;
//    }
//
//    default String userToAvatarUrl(Users user) {
//        return (user != null) ? user.getAvatarUrl() : null;
//    }
}
