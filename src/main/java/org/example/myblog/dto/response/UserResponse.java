package org.example.myblog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.Builder;
import org.example.myblog.model.Users;

import java.io.Serializable;

/**
 * DTO for {@link Users}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        Long id,
        String username,
        @Nullable String avatarUrl
) implements Serializable {
}
