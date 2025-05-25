package org.example.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.myblog.dto.request.UserRequest;
import org.example.myblog.dto.response.UserResponse;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.model.Users;
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
@Tag(name = "用户相关接口")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody UserRequest userRequest, HttpServletResponse response) {
        Long userId = userService.login(userRequest.username(), userRequest.password());
        if (userId != null) {
            CookieUtil.setCookie(response, jwtUtils.generateToken(userId));
            Users usersById = userRepository.findUserById(userId);
            return ApiResponse.success(UserResponse.builder()
                    .id(usersById.getId())
                    .username(usersById.getUsername())
                    .avatarUrl(usersById.getAvatarUrl())
                    .build());
        } else {
            throw new BusinessException(UserError.INVALID_USER);
        }
    }

    @Operation(summary = "注册接口", description = " 注册的用户名必须是英文,数字或者下划线.")
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody UserRequest userRequest) {
        userService.register(userRequest.username(), userRequest.password());
        return ApiResponse.success(null);
    }

    @Operation(summary = "头像上传", description = "用于上传头像, 上传一个头像, 返回可以访问该头像的地址")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file, @RequestAttribute Long userId) throws IOException {
        String uploadPath = userService.upload(file, userId);
        return ApiResponse.success(uploadPath);
    }


    @Operation(summary = "更新用户", description = "用于更新用户, 如果某个值不想更新, 那么不用传, 或者填入空值")
    @PostMapping("/update")
    public ApiResponse<Void> update(@RequestBody UserRequest userRequest, @RequestAttribute Long userId) {
        userService.update(userRequest, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        CookieUtil.deleteCookie(response);
        return ApiResponse.success(null);
    }
}

