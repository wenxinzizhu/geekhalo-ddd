package com.geekhalo.ddd.lite.domain.util;

import java.time.Instant;

public class ConvertUtils {
    public static Long toEpochMilli(Instant instant){
        return instant == null ? null : instant.toEpochMilli();
    }
}
