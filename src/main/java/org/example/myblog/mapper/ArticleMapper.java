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
    ArticleHomeResponse articleToArticleHomeResponse(Article article);

    Article createArticleRequestToArticle(CreateArticleRequest createArticleRequest);

    @Mapping(target = "commentIds", source = "comments")
    ArticleDetailResponse articleToArticleDetailResponse(Article article);

    default List<Long> mapComments(List<Comment> comments) {
        return Optional.ofNullable(comments).orElse(Collections.emptyList())
                .stream().map(Comment::getId).collect(Collectors.toList());
    }
}
