package org.example.myblog.dto.response;

import java.io.Serializable;

public record AllArticleResponse(
        Long articleId,
        String title
) implements Serializable {
}
