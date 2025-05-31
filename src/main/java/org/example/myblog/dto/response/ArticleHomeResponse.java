package org.example.myblog.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */

public record ArticleHomeResponse(
        Long id,
        String title,
        String cover_url,
        Boolean status,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        Long usersId,
        Long commentLength,
        String summarize) implements Serializable {
}
