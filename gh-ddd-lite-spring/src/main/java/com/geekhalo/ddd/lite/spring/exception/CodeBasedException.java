package com.geekhalo.ddd.lite.spring.exception;

import lombok.Data;

@Data
public class CodeBasedException extends RuntimeException {
    private Integer code;
    private String msg;
    private Object data;

    public CodeBasedException(){
        super();
    }
    public CodeBasedException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CodeBasedException(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CodeBasedException(String message, Integer code, String msg, Object data) {
        super(message);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


}
