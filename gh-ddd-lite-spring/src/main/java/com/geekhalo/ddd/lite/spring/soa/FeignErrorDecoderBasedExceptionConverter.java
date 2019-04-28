package com.geekhalo.ddd.lite.spring.soa;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class FeignErrorDecoderBasedExceptionConverter implements ErrorDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignErrorDecoderBasedExceptionConverter.class);

    public FeignErrorDecoderBasedExceptionConverter() {
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        Map<String, Collection<String>> headers = response.headers();
        return checkException(headers);
    }

    private Exception checkException(Map<String, Collection<String>> headers) {
        String code = getValue(headers, SoaConstants.HEADER_ERROR_CODE);

        String msg = HeaderValueUtils.decode(getValue(headers, SoaConstants.HEADER_ERROR_MSG));
        String exceptionMsg = HeaderValueUtils.decode(getValue(headers, SoaConstants.HEADER_ERROR_EXCEPTION_MSG));


        Integer errorCode = NumberUtils.isNumber(code) ? Integer.valueOf(code) : null;
        return new SoaRemoteCallException(exceptionMsg, errorCode, msg, "");
    }

    private String getValue(Map<String, Collection<String>> headers, String key) {
        Collection<String> values = headers.get(key);
        if (values != null && values.size() == 1){
            return values.iterator().next();
        }
        LOGGER.debug("failed to find value of {} in header {}", key, headers);
        return null;
    }


}
