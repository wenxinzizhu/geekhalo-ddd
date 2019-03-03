package com.geekhalo.ddd.lite.codegen.config;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taoli on 2019/3/3.
 */
public class CodeGenConfig {
    private static final String FILE_PATH = "/META-INF/ddd-gen.properties";
    private final Map<String, String> config = new HashMap<>();

    public String getByKey(String key){
        String value =  config.get(key);
        if (StringUtils.isNoneBlank(value)){
            return value;
        }
        return config.get(key + "_default");
    }

    public void init(){

    }
}
