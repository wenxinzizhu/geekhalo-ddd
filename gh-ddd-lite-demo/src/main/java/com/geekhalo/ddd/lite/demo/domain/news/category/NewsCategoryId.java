package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.domain.support.mongo.MongoEntityId;
import com.geekhalo.ddd.lite.domain.util.UUIDUtil;


public class NewsCategoryId extends MongoEntityId {
    private NewsCategoryId(){

    }
    private NewsCategoryId(String id){
        setValue(id);
    }

    public static NewsCategoryId apply(String id){
        return new NewsCategoryId(id);
    }

    public static NewsCategoryId create(){
        return  apply(UUIDUtil.genUUID());
    }
}
