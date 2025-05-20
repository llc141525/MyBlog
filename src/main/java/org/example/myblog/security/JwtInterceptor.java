package org.example.myblog.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
                    "/images/**",
                    "/user/log*",
                    "/user/register",
                    "/v3/api-docs/**",
                    "/swagger-ui*",
                    "/swagger-resources/**",
                    "/webjars/**"));

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        if (request.getMethod().equals("OPTIONS")) {
//            return true;
//        }
//          拦截响应, 提取出 cookie 把他放到 httpAttribute
//    }
}
