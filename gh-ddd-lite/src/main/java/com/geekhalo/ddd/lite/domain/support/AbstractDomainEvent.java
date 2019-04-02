package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.geekhalo.ddd.lite.domain.util.UUIDUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by taoli on 17/11/16.
 */
public abstract class AbstractDomainEvent implements DomainEvent {
    private final String id;
    private final Date createTime;

    protected AbstractDomainEvent(String id) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(id));
        this.id = id;
        this.createTime = new Date();
    }

    protected AbstractDomainEvent(){
        this(UUIDUtil.genUUID());
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Date occurredOn() {
        return this.createTime;
    }

    //    private final A source;
//
//    public AbstractDomainEvent(A source) {
//        Preconditions.checkArgument(source != null);
//        this.source = source;
//    }
//
//    @Override
//    public A getSource() {
//        return source;
//    }
}
