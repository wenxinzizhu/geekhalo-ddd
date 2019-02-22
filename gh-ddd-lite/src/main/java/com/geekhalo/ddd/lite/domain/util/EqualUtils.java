package com.geekhalo.ddd.lite.domain.util;

import java.util.Map;
import java.util.Set;

/**
 * Created by fw on 2018/4/19
 */
public final class EqualUtils {
    private EqualUtils() {
    }


    public  static boolean equals(Map<String ,Object> expected, Map<String ,Object> actual){
         if ( expected == actual)
             return true;
         if( expected==null || actual == null)
             return false;
         if ( expected.size() != actual.size())
             return false;
         Set<Map.Entry<String, Object>> entries = expected.entrySet();
         for (Map.Entry entry:entries) {
             Object key = entry.getKey();
             Object valExpected = entry.getValue();
             Object valActual = actual.get(key);

             assert equals(valExpected,valActual);

             if (!equals(valExpected,valActual)) {
                 return false;
             }
         }

         return  true;
     }

    public static  boolean equals(Object expected, Object actual){
         if ( expected == actual)
             return true;
         if( expected==null || actual == null)
             return false;
         return expected.equals(actual);
     }
}
