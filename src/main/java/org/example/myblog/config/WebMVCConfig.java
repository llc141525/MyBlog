package org.example.myblog.config;

import lombok.RequiredArgsConstructor;
import org.example.myblog.security.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebMVCConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Value("${pathMap.accessPath}")
    private String accessPath;

    @Value("${pathMap.uploadDir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 匹配所有路径
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("*") // 允许所有HTTP方法
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")               // 拦截所有路径
                .excludePathPatterns(
                        "/user/log**",
                        "/user/register**",
                        "/article/home/**",
                        "/article/detail/**",
                        "/comment/get/**",
                        "/static/**",
                        "/v3/api-docs/**",
                        "/img/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**");
    }

    /*添加路径映射*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString().replace("\\", "/");
        registry.addResourceHandler(accessPath)
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
