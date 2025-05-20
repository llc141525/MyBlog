package org.example.myblog.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.myblog.utils.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    // 处理数据层异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataException(DataIntegrityViolationException ex) {
        log.error("数据库异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, "数据冲突，请检查任务名称唯一性"));
    }

    // 自定义的异常处理

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage()));
    }

    // 参数校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse("参数错误")
                ));
        return ApiResponse.error(400, "参数校验失败", errors);
    }

    // 处理参数缺失异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = "缺少必需参数: " + ex.getParameterName();
        log.error(message);
        return ApiResponse.error(400, message);
    }

    // 处理类型不匹配异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "参数类型错误: " + ex.getName() +
                " 应为 " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        log.error(message);
        return ApiResponse.error(400, message);
    }

    // 处理参数校验失败异常（@RequestParam、@PathVariable）
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<String> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    // 处理 JSON 解析异常（如日期格式错误）
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("JSON 解析错误: {}", ex.getMessage());

        // 提取具体错误信息
        Throwable rootCause = ex.getRootCause();
        String errorMessage = "请求体格式错误, 请检查是否有必填的字段缺失";
        if (rootCause instanceof InvalidFormatException ife) {
            errorMessage = String.format("字段 '%s' 格式错误，应为 %s",
                    ife.getPath().getFirst().getFieldName(),
                    ife.getTargetType().getSimpleName());
        } else if (rootCause instanceof DateTimeParseException) {
            errorMessage = "日期格式错误，正确格式应为 yyyy-MM-dd HH:mm";
        }
        return ApiResponse.error(400, errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Void>> handleIOException(IOException ex) {
        log.error("IO 异常", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error(400, "IO 异常"));
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
