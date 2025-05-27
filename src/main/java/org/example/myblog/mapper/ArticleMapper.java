package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.response.ArticleDetailResponse;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.model.Article;
import org.example.myblog.model.Comment;
import org.example.myblog.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "usersId", source = "users")
    @Mapping(target = "commentLength", source = "comments")
    ArticleHomeResponse articleToArticleHomeResponse(Article article);


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
    @Mapping(target = "usersAvatarUrl", source = "users")
    @Mapping(target = "usersId", source = "users")
    ArticleDetailResponse articleToArticleDetailResponse(Article article);

    default List<Long> mapComments(List<Comment> comments) {
        return Optional.ofNullable(comments).orElse(Collections.emptyList())
                .stream().map(Comment::getId).collect(Collectors.toList());
    }

    default Long userToId(Users user) {
        return (user != null) ? user.getId() : null;
    }

    default String userToAvatarUrl(Users user) {
        return (user != null) ? user.getAvatarUrl() : null;
    }
}
