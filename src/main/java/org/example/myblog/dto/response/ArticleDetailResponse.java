package org.example.myblog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ArticleDetailResponse(
        Long id,
        String title,
        String content,
        String cover_url,
        Boolean status,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        Long usersId,
        String usersAvatarUrl,
        List<Long> commentIds
) implements Serializable {
}