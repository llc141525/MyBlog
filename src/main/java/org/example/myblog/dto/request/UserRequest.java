package org.example.myblog.dto.request;

import java.io.Serializable;

/**
 * DTO for {@link org.example.myblog.model.User}
 */
public record UserRequest(String username,
                          String password) implements Serializable {
}