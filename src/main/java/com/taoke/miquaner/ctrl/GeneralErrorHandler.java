package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GeneralErrorHandler {

    //other exception handlers

    @ExceptionHandler(Exception.class)
    protected Object handleException(Exception ex) {
        return Result.fail(new ErrorR(ErrorR.CAN_NOT_SAVE_OBJECT, ErrorR.CAN_NOT_SAVE_OBJECT_MSG));
    }

}
