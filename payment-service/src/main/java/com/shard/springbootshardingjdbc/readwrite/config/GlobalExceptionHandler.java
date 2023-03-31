package com.shard.springbootshardingjdbc.readwrite.config;

import com.shard.shardingjdbc.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handle(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(null,e.getMessage());
    }

}
