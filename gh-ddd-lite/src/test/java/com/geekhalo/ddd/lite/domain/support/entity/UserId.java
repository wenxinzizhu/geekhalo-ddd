package com.geekhalo.ddd.lite.domain.support.entity;

import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntityId;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class UserId extends JpaEntityId {
    public static UserId apply(String id){
        UserId userId = new UserId();
        userId.setValue(id);
        return userId;
    }
}
