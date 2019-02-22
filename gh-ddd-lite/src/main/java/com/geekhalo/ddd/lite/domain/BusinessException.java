package com.geekhalo.ddd.lite.domain;

import lombok.Data;

/**
 * Created by taoli on 17/11/17.
 */
@Data
public class BusinessException extends RuntimeException{
    private final Integer code;
    private final String msg;


    public BusinessException(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public BusinessException(Integer code, String message, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.msg = message;
    }

}
