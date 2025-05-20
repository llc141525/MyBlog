package org.example.myblog.service;

import lombok.RequiredArgsConstructor;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.model.User;
import org.example.myblog.repository.UserRepository;
import org.example.myblog.security.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    public void register(String username, String password) {
        // 如果用户名使用非法字符, 用户名必须是字母或者数字或者下划线
        if (!username.matches("[a-zA-Z0-9_]{3,12}")) {
            throw new BusinessException(
                    UserError.INVALID_USERNAME.getCode(),
                    UserError.INVALID_USERNAME.getMessage());
        }

        // 如果重复注册相同的用户名
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(
                    UserError.DUPLICATE_USER.getCode(),
                    UserError.DUPLICATE_USER.getMessage());
        }

        User newUser = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        userRepository.save(newUser);
    }

    public Long login(String username, String password) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            throw new BusinessException(
                    UserError.INVALID_USER.getCode(),
                    UserError.INVALID_USER.getMessage());
        }
        if (bCryptPasswordEncoder.matches(password, byUsername.getPassword())) {
            return byUsername.getId();
        } else {
            throw new BusinessException(
                    UserError.INVALID_USER.getCode(),
                    UserError.INVALID_USER.getMessage());
        }
    }

}
