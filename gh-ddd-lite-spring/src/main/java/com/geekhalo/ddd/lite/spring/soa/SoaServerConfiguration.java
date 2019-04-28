package com.geekhalo.ddd.lite.spring.soa;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SoaServerConfiguration {

    @Configuration
    public class SoaWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HandlerInterceptorBasedExceptionBinder()).addPathPatterns("/**");
        }
    }

}
