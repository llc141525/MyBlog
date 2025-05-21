package org.example.myblog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.User}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(Long id, String username, @Nullable String avatarUrl) implements Serializable {
}