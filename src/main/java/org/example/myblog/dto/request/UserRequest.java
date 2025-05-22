package org.example.myblog.dto.request;

import org.example.myblog.model.Users;

import java.io.Serializable;

/**
 * DTO for {@link Users}
 */
public record UserRequest(String username,
                          String password) implements Serializable {
}
