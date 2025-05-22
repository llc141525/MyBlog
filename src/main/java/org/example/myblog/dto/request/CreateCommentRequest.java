package org.example.myblog.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateCommentRequest(
        @NotNull(message = "没有传入文章id") Long articleId,
        @Nullable Long parentCommentId,
        @NotNull(message = "没有用户id, 应该是没有登录") Long usersId,
        @NotBlank(message = "评论不能是空白的") String content
) implements Serializable {
}