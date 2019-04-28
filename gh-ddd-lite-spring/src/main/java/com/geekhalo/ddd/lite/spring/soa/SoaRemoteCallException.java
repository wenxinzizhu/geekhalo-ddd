package com.geekhalo.ddd.lite.spring.soa;


import com.geekhalo.ddd.lite.spring.exception.CodeBasedException;

public class SoaRemoteCallException extends CodeBasedException {
    public SoaRemoteCallException(String message, Integer code, String msg, Object data) {
        super(message, code, msg, data);
    }
}
