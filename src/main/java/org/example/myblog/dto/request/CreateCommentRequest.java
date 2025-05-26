package org.example.myblog.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateCommentRequest(
        @Schema(example = "1")
        @NotNull(message = "没有传入文章id")
        Long articleId,

        @Nullable
        Long parentCommentId,

        @Schema(example = "测试评论")
        @NotBlank(message = "评论不能是空白的")
        String content
) implements Serializable {
}