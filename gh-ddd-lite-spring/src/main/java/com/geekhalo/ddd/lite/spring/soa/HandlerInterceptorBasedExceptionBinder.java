package com.geekhalo.ddd.lite.spring.soa;

import com.geekhalo.ddd.lite.domain.BusinessException;
import com.geekhalo.ddd.lite.spring.exception.CodeBasedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.geekhalo.ddd.lite.spring.soa.HeaderValueUtils.encode;


public class HandlerInterceptorBasedExceptionBinder implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerInterceptorBasedExceptionBinder.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex == null){
            return;
        }

        if (ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            response.addHeader(SoaConstants.HEADER_ERROR_CODE, String.valueOf(businessException.getCode()));
            response.addHeader(SoaConstants.HEADER_ERROR_MSG, encode(businessException.getMsg()));

            response.addHeader(SoaConstants.HEADER_ERROR_EXCEPTION_MSG, encode(businessException.getMessage()));
            return;
        }
        if (ex instanceof CodeBasedException){
            CodeBasedException codeBasedException = (CodeBasedException) ex;
            response.addHeader(SoaConstants.HEADER_ERROR_CODE, String.valueOf(codeBasedException.getCode()));
            response.addHeader(SoaConstants.HEADER_ERROR_MSG, encode(codeBasedException.getMsg()));

            response.addHeader(SoaConstants.HEADER_ERROR_EXCEPTION_MSG, encode(codeBasedException.getMessage()));
            return;

        }

        response.setHeader(SoaConstants.HEADER_ERROR_CODE, "500");
        response.setHeader(SoaConstants.HEADER_ERROR_MSG, encode(ex.getMessage()));

        response.setHeader(SoaConstants.HEADER_ERROR_EXCEPTION_MSG, encode(String.valueOf(ex.getStackTrace())));
        LOGGER.error("failed to handle request.", ex);
    }


}
