package org.example.myblog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.myblog.dto.request.UserRequest;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.UserError;
import org.example.myblog.model.Users;
import org.example.myblog.repository.UserRepository;
import org.example.myblog.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${pathMap.uploadDir}")
    private String uploadDir;

    @Value("${pathMap.accessPath}")
    private String accessPath;

    @Transactional
    public void register(String username, String password) {
        // 如果用户名使用非法字符, 用户名必须是字母或者数字或者下划线
        if (!username.matches("[a-zA-Z0-9_]{3,12}")) {
            throw new BusinessException(UserError.INVALID_USERNAME);
        }

        // 如果重复注册相同的用户名
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(UserError.DUPLICATE_USER);
        }

        Users newUsers = Users.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        userRepository.save(newUsers);
    }

    public Long login(String username, String password) {
        Users byUsername = userRepository.findByUsername(username);
        // 如果用户不存在
        if (byUsername == null) {
            throw new BusinessException(UserError.INVALID_USER);
        }
        if (bCryptPasswordEncoder.matches(password, byUsername.getPassword())) {
            return byUsername.getId();
        } else {
            // 如果密码错误
            throw new BusinessException(UserError.INVALID_USER);
        }
    }

    @Transactional
    public void update(UserRequest updateUserRequest, Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // 修改密码
        Optional.ofNullable(updateUserRequest.password())
                .map(bCryptPasswordEncoder::encode)
                .ifPresent(password -> {
                    if (!password.isBlank()) {
                        users.setPassword(password);
                    }
                });

        // 修改用户名
        Optional.ofNullable(updateUserRequest.username())
                .ifPresent(username -> {
                    if (username.isBlank()) return;

                    // 如果更新用户的字符是非法字符的话
                    if (!username.matches("[0-9a-zA-Z_]{3,12}"))
                        throw new BusinessException(UserError.INVALID_USERNAME);
                    users.setUsername(username);
                });
    }

    @Transactional
    public String upload(MultipartFile avatar, Long userId) throws IOException {
        // 如果文件是空文件, 就抛出异常
        if (avatar == null || avatar.isEmpty()) {
            throw new BusinessException(UserError.EMPTY_FILE);
        }

        // 修改文件名为随机文件名
        String originalFilename = avatar.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            extension = StringUtils.getFilenameExtension(originalFilename);
            // 文件类型异常
            if (extension != null && !Set.of("jpg", "jpeg", "png", "gif").contains(extension.toLowerCase())) {
                throw new BusinessException(UserError.INVALID_FILE_TYPE);
            }
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String randomFileName = uuid + '.' + extension;


        // 如果没有对应的文件目录, 就新建一个.
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件到本地
        Path filePath = uploadPath.resolve(randomFileName);
        avatar.transferTo(filePath);

        String path = "http://localhost:8080" + accessPath.replace("**", "") + randomFileName;
        userRepository.findById(userId).ifPresent(user -> {
            // 如果原本头像不为空, 那么替换头像的时候需要把原本的头像删除
            String oldAvatarUrl = user.getAvatarUrl();
            if (StringUtils.hasText(oldAvatarUrl)) {
                try {
                    deleteOldFiles(oldAvatarUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user.setAvatarUrl(path);
            userRepository.save(user);
        });
        return path;
    }

    // 异步删除, 如果删除失败也不会影响主线程.
    @Transactional
    @Async
    public void deleteOldFiles(String path) throws IOException {
        String oldFileName = UriComponentsBuilder.fromUriString(path).build().getPathSegments().getLast();
        Path filePath = Paths.get(uploadDir, oldFileName);
        try {
            if (Files.deleteIfExists(filePath)) {
                log.info("删除成功");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
