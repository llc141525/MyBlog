//package org.example.myblog.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
/// /@NoArgsConstructor
//public class JwtUtils {
//    // 通过 Base64 编码的密钥（推荐环境变量注入）
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expirationSeconds;
//
//
//    // 生成 Token
//    public String generateToken(Long userId) {
//        return Jwts.builder().claim("userId", userId)                 // 用户标识（推荐用唯一ID）
//                .issuedAt(new Date())               // 签发时间
//                .expiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000)) // 过期时间
//                .signWith(getSecretKey())           // 密钥签名
//                .compact();
//    }
//
//    public Claims parseToken(String token) {
//        try {
//            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
//        } catch (ExpiredJwtException e) {
//            throw new SecurityException("Token 已过期", e);
//        } catch (MalformedJwtException e) {
//            throw new SecurityException("Token 格式错误", e);
//        } catch (JwtException e) {
//            throw new SecurityException("Token 验证失败", e);
//        }
//    }
//
//    // 获取安全的密钥对象
//    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//}
