package com.geekhalo.ddd.lite.domain.support.jpa;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class JpaEntityVo {
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

    protected JpaEntityVo(JpaEntity source) {
        this.setVersion(source.getVersion());
        this.setId(source.getId());
        this.setCreateTimeAsMS(source.getCreateTimeAsMS());
        this.setUpdateTimeAsMS(source.getUpdateTimeAsMS());
    }

    protected JpaEntityVo() {
    }
}
