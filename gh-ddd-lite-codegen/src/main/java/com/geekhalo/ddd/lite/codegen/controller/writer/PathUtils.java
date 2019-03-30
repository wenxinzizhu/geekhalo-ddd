package com.geekhalo.ddd.lite.codegen.controller.writer;

public class PathUtils {
    public static final String getPathFromMethod(String methodName){
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : methodName.toCharArray()){
            if (Character.isUpperCase(c)){
                stringBuilder.append("-").append(Character.toLowerCase(c));
            }else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
