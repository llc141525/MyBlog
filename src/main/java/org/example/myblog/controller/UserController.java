package org.example.myblog.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户相关代码")
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
            throw new BusinessException(UserError.INVALID_USER);
        }
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody UserRequest userRequest) {
        userService.register(userRequest.username(), userRequest.password());
        return ApiResponse.success(null);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file, @RequestAttribute Long userId) throws IOException {
        String uploadPath = userService.upload(file, userId);
        return ApiResponse.success(uploadPath);
    }


    @PostMapping("/update")
    public ApiResponse<Void> update(@RequestBody UserRequest userRequest, @RequestAttribute Long userId) {
        userService.update(userRequest, userId);
        return ApiResponse.success(null);
    }
}

