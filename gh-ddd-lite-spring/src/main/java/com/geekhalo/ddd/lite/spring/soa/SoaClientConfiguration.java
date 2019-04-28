package com.geekhalo.ddd.lite.spring.soa;

import org.springframework.context.annotation.Bean;

public class SoaClientConfiguration {

    @Bean
    public FeignErrorDecoderBasedExceptionConverter exceptionCheckFeignDecoder(){
        return new FeignErrorDecoderBasedExceptionConverter();
    }
}
