package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;

/**
 * Created by fw on 2018/4/9
 */
public class GenericStringConverter implements AttributeConverter<Object,String> {

        @Override
        public String convertToDatabaseColumn(Object data) {
            return JSON.toJSONString(data);
        }

        @Override
        public Object convertToEntityAttribute(String data) {
            return JSON.parse(data);
        }
}
