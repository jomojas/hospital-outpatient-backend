package com.ncst.hospitaloutpatient.common.handler;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.ncst.hospitaloutpatient.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(40002, message); // 40002 缺失参数
    }

    // 可以继续添加其它类型异常的处理
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleOtherException(Exception e) {
        // 日志可加
        return ApiResponse.error(500, "服务器内部错误");
    }
}