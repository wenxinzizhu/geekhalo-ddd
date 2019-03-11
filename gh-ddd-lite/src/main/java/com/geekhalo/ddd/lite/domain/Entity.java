package com.geekhalo.ddd.lite.domain;

import java.util.Date;

/**
 * Created by taoli on 17/11/16.
 */
public interface Entity<ID> extends Validator{
    /**
     * 获取实体唯一主键
     * @return
     */
    ID getId();

    /**
     * 获取实体当前版本号
     * @return
     */
    int getVersion();

    /**
     * 获取实体创建时间
     * @return
     */
    Date getCreateTime();


    /**
     * 获取实体更新时间
     * @return
     */
    Date getUpdateTime();
}
