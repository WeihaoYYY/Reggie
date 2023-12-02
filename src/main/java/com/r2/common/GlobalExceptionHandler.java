package com.r2.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

// @ControllerAdvice注解的作用是：全局异常处理，拦截带RestController注解的类的异常
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    // @ExceptionHandler注解的作用是：指明要拦截的异常
    @ExceptionHandler(CustomException.class)
    public R<String> handleException(CustomException ex) {
        log.error("SQL异常: {}", ex.getMessage());

        return R.error(ex.getMessage());
    }


}
