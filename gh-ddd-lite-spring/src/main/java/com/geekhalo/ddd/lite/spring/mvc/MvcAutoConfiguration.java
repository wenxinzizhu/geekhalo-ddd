package com.geekhalo.ddd.lite.spring.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ConditionalOnWebApplication
@Configuration
public class MvcAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MvcAutoConfiguration.class);
    @Configuration
    public class WebMvcConfiguration implements WebMvcRegistrations {
        @Override
        public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
            return new InheritBasedRequestMappingHandlerMapping();
        }
    }

    @ControllerAdvice
    public class MvcControllerAdvice {
        @ExceptionHandler
        public ResultVo<Void> handleException(Exception e){
            LOGGER.error("failed to handle request.", e);
            return ResultVo.error(e);
        }
    }

    class InheritBasedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
            Class dClass = method.getDeclaringClass();
            if (Modifier.isAbstract(dClass.getModifiers())){
                return null;
            }
            return super.getMappingForMethod(method, handlerType);
        }
    }

}