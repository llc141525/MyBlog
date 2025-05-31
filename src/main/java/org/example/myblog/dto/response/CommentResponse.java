package org.example.myblog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link org.example.myblog.model.Comment}
 */

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentResponse(
        LocalDateTime createTime,
        List<CommentDto> childComment,
        Long usersId,
        String usersUsername,
        String usersAvatarUrl,
        Long id,
        String content,
        String username
) implements Serializable {
    /**
     * DTO for {@link org.example.myblog.model.Comment}
     */
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CommentDto(
            LocalDateTime createTime,
            Long id,
            String content,
            String username,
            String avatar
    ) implements Serializable {
    }
}