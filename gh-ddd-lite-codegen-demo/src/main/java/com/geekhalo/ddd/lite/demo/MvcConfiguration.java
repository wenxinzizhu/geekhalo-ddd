package com.geekhalo.ddd.lite.demo;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Configuration
public class MvcConfiguration {

    @Configuration
    public class WebMvcConfiguration implements WebMvcRegistrations {
        @Override
        public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
            return new InheritBasedRequestMappingHandlerMapping();
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