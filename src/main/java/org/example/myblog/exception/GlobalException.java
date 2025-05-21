package org.example.myblog.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.myblog.exception.errors.ErrorCode;
import org.example.myblog.utils.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    // 处理数据层异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataException(DataIntegrityViolationException ex) {
        log.error("数据库异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT.value(), "数据冲突，请检查数据唯一性"));
    }

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("业务异常: [{}], {}", errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }


    // 统一处理其他客户端错误（400状态）
    @ExceptionHandler({
            MissingServletRequestParameterException.class, // 缺少必要参数
            MethodArgumentTypeMismatchException.class, // 处理类型不匹配异常
            ConstraintViolationException.class, //处理参数校验失败异常（@RequestParam、@PathVariable）
            HttpMessageNotReadableException.class, //处理 JSON 解析异常（如日期格式错误）
            MethodArgumentNotValidException.class //参数校验异常
    })
    public ResponseEntity<ApiResponse<?>> handleClientErrors(Exception ex) {
        String message = resolveClientErrorMsg(ex);
        log.warn("客户端请求错误: {}", message);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    // 解析客户端错误信息
    private String resolveClientErrorMsg(Exception ex) {
        if (ex instanceof MissingServletRequestParameterException missingEx) {
            return "缺少必需参数: " + missingEx.getParameterName();
        } else if (ex instanceof MethodArgumentTypeMismatchException mismatchEx) {
            return String.format("参数 '%s' 类型错误，应为 %s",
                    mismatchEx.getName(),
                    Objects.requireNonNull(mismatchEx.getRequiredType()).getSimpleName());
        } else if (ex instanceof ConstraintViolationException violationEx) {
            return violationEx.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
        } else if (ex instanceof HttpMessageNotReadableException) {
            return "请求体解析失败，请检查数据格式";
        } else if (ex instanceof MethodArgumentNotValidException) {
            return "参数校验异常";
        }
        return "客户端请求错误";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Void>> handleIOException(IOException ex) {
        log.error("IO 异常", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO 异常"));
    }

    // 兜底异常处理
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error("系统异常: ", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "系统繁忙，请稍后重试"));
    }
}
