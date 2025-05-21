package org.example.myblog.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.myblog.exception.BusinessException;
import org.example.myblog.exception.errors.UserError;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> EXCLUDE_PATH =
            new ArrayList<>(Arrays.asList(
                    "/img/**",
                    "/user/log*",
                    "/user/register",
                    "/v3/api-docs/**",
                    "/swagger-ui*",
                    "/swagger-resources/**",
                    "/webjars/**"));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String requestURI = request.getRequestURI();
        if (EXCLUDE_PATH.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI))) {
            return true;
        }

//      提取出 cookie 把他放到 httpAttribute
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("my_blog_token")) {
                    Long userId = jwtUtils.parseToken(cookie.getValue()).get("userId", Long.class);
                    request.setAttribute("userId", userId);
                    return true;
                }
            }
        } else {
            throw new BusinessException(UserError.NO_COOKIE);
        }
        return false;
    }
}
