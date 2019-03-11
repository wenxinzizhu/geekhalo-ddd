package com.geekhalo.ddd.lite.demo.domain.news.info;

import com.geekhalo.ddd.lite.domain.support.mongo.MongoEntityId;
import com.geekhalo.ddd.lite.domain.util.UUIDUtil;

public class NewsInfoId extends MongoEntityId {
    private NewsInfoId(){

    }
    private NewsInfoId(String id){
        super(id);
    }
    public static NewsInfoId apply(String id){
        return new NewsInfoId(id);
    }

    public static NewsInfoId create(){
        return apply(UUIDUtil.genUUID());
    }
}
