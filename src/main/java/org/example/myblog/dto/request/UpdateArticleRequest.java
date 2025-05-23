package org.example.myblog.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.Article}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateArticleRequest(
        @Schema(example = "1")
        Long id,
        
        @Schema(example = "测试更新文章标题")
        String title,

        @Schema(example = "测试更新文章内容")
        String content,

        @Schema(example = "http://test_update_url")
        String cover_url,

        @Schema(example = "false")
        Boolean status
) implements Serializable {
}