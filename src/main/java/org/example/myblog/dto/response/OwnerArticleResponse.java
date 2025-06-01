package org.example.myblog.dto.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OwnerArticleResponse(
        Long articleId,
        String title,
        String summarize,
        Boolean status) implements Serializable {
}
