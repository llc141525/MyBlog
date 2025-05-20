package org.example.myblog.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.User}
 */
public record UserRequest(@NotBlank String username,
                          @NotBlank(message = "密码不能为空") String password) implements Serializable {
}