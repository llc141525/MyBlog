package org.example.myblog.utils;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static void setCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("my_blog_token", token)
                .httpOnly(true)                                 // 禁止 js 访问
                .path("/")                                      // 全路径可访问
                .maxAge(3600)                      // 设置 cookie 寿命为 3600 秒
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("my_blog_token", "")
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}