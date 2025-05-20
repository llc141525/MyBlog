package org.example.myblog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.User}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(Long id, String username, String avatarUrl) implements Serializable {
}