package com.geekhalo.ddd.lite.domain;

import java.util.Date;

/**
 * Created by taoli on 17/11/16.
 */
public interface Entity<ID> extends Validator{
    ID getId();

    int getVersion();

    Date getCreateTime();

    Date getUpdateTime();
}
