package org.example.myblog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */

public record CreateArticleRequest(
        @NotEmpty(message = "标题不能为空")
        @Schema(example = "测试文章标题")
        String title,

        @Schema(example = "测试文章的内容")
        @NotEmpty(message = "文章内容不能为空")
        String content,

        @Schema(example = "http://test_url")
        String cover_url,

        @Schema(example = "false")
        @NotNull(message = "缺少发布状态, 草稿或者已发布")
        Boolean status
) implements Serializable {
}
