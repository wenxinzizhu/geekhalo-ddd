package com.geekhalo.ddd.lite.domain.support.vo;

import com.geekhalo.ddd.lite.domain.ValueObject;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter(AccessLevel.PRIVATE)
@Data
@Embeddable
public class Mobile implements ValueObject {
    public static final String DEFAULT_DCC = "0086";
    @Column(name = "dcc")
    private String dcc;
    @Column(name = "mobile")
    private String mobile;


    private Mobile() {
    }

    private Mobile(String dcc, String mobile){
        Preconditions.checkArgument(StringUtils.isNotEmpty(dcc));
        Preconditions.checkArgument(StringUtils.isNotEmpty(mobile));
        setDcc(dcc);
        setMobile(mobile);
    }

    public static Mobile apply(String mobile){
        return apply(DEFAULT_DCC, mobile);
    }

    public static Mobile apply(String dcc, String mobile){
        return new Mobile(dcc, mobile);
    }

}
