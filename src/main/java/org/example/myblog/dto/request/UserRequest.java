package org.example.myblog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.myblog.model.Users;

import java.io.Serializable;

/**
 * DTO for {@link Users}
 */
public record UserRequest(
        @Schema(example = "abc")
        String username,
        @Schema(example = "123")
        String password
) implements Serializable {
}
