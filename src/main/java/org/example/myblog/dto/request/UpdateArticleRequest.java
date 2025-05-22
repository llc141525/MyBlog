package org.example.myblog.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateArticleRequest(
        Long id,
        String title,
        String content,
        String cover_url,
        Boolean status
) implements Serializable {
}