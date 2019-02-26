package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntityId;

import javax.persistence.Embeddable;

@Embeddable
public class NewsCategoryId extends JpaEntityId {

    public static NewsCategoryId apply(String id){
        NewsCategoryId newsCategoryId = new NewsCategoryId();
        newsCategoryId.setValue(id);
        return newsCategoryId;
    }
}
