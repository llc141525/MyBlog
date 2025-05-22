package org.example.myblog.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */

public record CreateArticleRequest(@NotEmpty(message = "标题不能为空")
                                   String title,
                                   @NotEmpty(message = "文章内容不能为空")
                                   String content,
                                   String cover_url,
                                   @NotNull(message = "缺少发布状态, 草稿或者已发布")
                                   Boolean status
) implements Serializable {
}
