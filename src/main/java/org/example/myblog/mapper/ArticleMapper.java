package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateArticleRequest;
import org.example.myblog.dto.response.AllArticleResponse;
import org.example.myblog.dto.response.ArticleDetailResponse;
import org.example.myblog.dto.response.ArticleHomeResponse;
import org.example.myblog.dto.response.OwnerArticleResponse;
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
    @Mapping(target = "cover_url", ignore = true)
//    @Mapping(target = "summarize", ignore = true)
    @Mapping(target = "summarize", expression = "java(truncateSummary(createArticleRequest))")
    Article createArticleRequestToArticle(CreateArticleRequest createArticleRequest);

    default String truncateSummary(CreateArticleRequest request) {
        if (request.summarize() == null || request.summarize().isEmpty()) {
            return request.content().substring(0, 100);
        } else {
            String summ = request.summarize()
                    .replaceAll("[ -~]", "");
            if (summ.length() > 100) return summ.substring(0, 100);
            else return summ;
        }
    }

    @Mapping(target = "commentIds", source = "comments")
    @Mapping(target = "usersAvatarUrl", source = "users.avatarUrl")
    @Mapping(target = "usersId", source = "users.id")
    ArticleDetailResponse articleToArticleDetailResponse(Article article);

    default List<Long> mapComments(List<Comment> comments) {
        return Optional.ofNullable(comments).orElse(Collections.emptyList())
                .stream().map(Comment::getId).collect(Collectors.toList());
    }

    @Mapping(target = "articleId", expression = "java(article.getId())")
    OwnerArticleResponse articleToOwnerArticleResponse(Article article);

    @Mapping(target = "articleId", expression = "java(article.getId())")
    AllArticleResponse articleToAllArticleResponse(Article article);
}
