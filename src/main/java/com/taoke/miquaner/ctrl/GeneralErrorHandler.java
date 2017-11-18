package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GeneralErrorHandler {

    private static final Logger logger = LogManager.getLogger(GeneralErrorHandler.class);

    @ExceptionHandler(Exception.class)
    protected Object handleException(Exception ex) {
        logger.error(ex.getClass().getName() + " " + ex.getMessage(), ex);
        return Result.fail(new ErrorR(ErrorR.CAN_NOT_SAVE_OBJECT, ErrorR.CAN_NOT_SAVE_OBJECT_MSG));
    }

}
