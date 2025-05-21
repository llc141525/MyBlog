package org.example.myblog.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */
@Builder
public record ArticleHomeResponse(Long id,
                                  String title,
                                  String cover_url,
                                  Boolean status,
                                  LocalDateTime createTime,
                                  LocalDateTime updateTime,
                                  Long userId
) implements Serializable {
}
