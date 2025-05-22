package org.example.myblog.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateCommentRequest(
        Long articleId,
        Long parentCommentId,
        Long usersId,
        String content
) implements Serializable {
}