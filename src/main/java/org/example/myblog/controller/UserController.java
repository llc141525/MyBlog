package org.example.myblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.UserRequest;
import org.example.myblog.dto.response.UserResponse;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.model.User;
import org.example.myblog.repository.UserRepository;
import org.example.myblog.security.JwtUtils;
import org.example.myblog.service.UserService;
import org.example.myblog.utils.ApiResponse;
import org.example.myblog.utils.CookieUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody UserRequest userRequest, HttpServletResponse response) {
        Long userId = userService.login(userRequest.username(), userRequest.password());
        if (userId != null) {
            CookieUtil.setCookie(response, jwtUtils.generateToken(userId));
            User userById = userRepository.findUserById(userId);
            return ApiResponse.success(UserResponse.builder()
                    .id(userById.getId())
                    .username(userById.getUsername())
                    .avatarUrl(userById.getAvatarUrl())
                    .build());
        } else {
            throw new BusinessException(UserError.INVALID_USER.getCode(), UserError.INVALID_USER.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody UserRequest userRequest) {
        userService.register(userRequest.username(), userRequest.password());
        return ApiResponse.success(null);
    }

}
