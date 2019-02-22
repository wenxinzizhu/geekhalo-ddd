package com.geekhalo.ddd.lite.domain.support.jpa;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class JpaAggregateVo extends JpaEntityVo{
    @ApiModelProperty(
            value = "数据版本",
            name = "version"
    )
    private int version;
    @ApiModelProperty(
            value = "主键",
            name = "id"
    )
    private Long id;
    @ApiModelProperty(
            value = "创建时间, MS",
            name = "createTimeAsMS"
    )
    private Long createTimeAsMS;
    @ApiModelProperty(
            value = "修改时间, MS",
            name = "updateTimeAsMS"
    )
    private Long updateTimeAsMS;

    protected JpaAggregateVo(JpaAggregate source) {
       super(source);
    }

    protected JpaAggregateVo() {
    }
}
