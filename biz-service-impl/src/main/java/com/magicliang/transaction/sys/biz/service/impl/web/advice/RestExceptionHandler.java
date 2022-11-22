package com.magicliang.transaction.sys.biz.service.impl.web.advice;

import com.magicliang.transaction.sys.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * project name: leads
 * <p>
 * description: 解决全部的异常处理问题
 *
 * @author magicliang
 * <p>
 * date: 2022-11-17 17:10
 */
@Slf4j
// 看这个注解被 @Component 注解，可以学到这个框架的一种用法
//@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<Object> handleAllException(RuntimeException ex, HttpServletRequest req) {
        final String uri = req.getRequestURI();
        if (ex instanceof BizException) {
            return new ResponseEntity<>(new HashMap<String, String>(0),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "接口调用出错",
                HttpStatus.OK);
    }
}
