package com.geekhalo.ddd.lite.domain.support;

public interface CodeBasedEnum<T extends Enum<T> & CodeBasedEnum<T>> {
    int getCode();

    static<T extends Enum<T> & CodeBasedEnum<T> > T parseByCode(Class<T> cls, int code){
        for (T t : cls.getEnumConstants()){
            if (t.getCode() == code){
                return t;
            }
        }
        return null;
    }
}
