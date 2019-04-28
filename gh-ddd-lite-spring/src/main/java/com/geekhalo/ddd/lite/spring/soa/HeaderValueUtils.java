package com.geekhalo.ddd.lite.spring.soa;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HeaderValueUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderValueUtils.class);

    public static String encode(String data){
        if (StringUtils.isNotEmpty(data)){
            try {
                return URLEncoder.encode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("failed to encode {}", data, e);
            }
        }
        return null;

    }

    public static String decode(String data){
        if (StringUtils.isNotEmpty(data)){
            try {
                return URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("failed to decode {}", data, e);
            }
        }
        return null;

    }
}
