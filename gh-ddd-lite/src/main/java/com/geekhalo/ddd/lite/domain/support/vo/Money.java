package com.geekhalo.ddd.lite.domain.support.vo;

import com.geekhalo.ddd.lite.domain.ValueObject;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@Setter(AccessLevel.PRIVATE)
@Embeddable
public class Money implements ValueObject {
    public static final String DEFAULT_FEE_TYPE = "CNY";
    @Column(name = "total_fee")
    private Integer totalFee;
    @Column(name = "fee_type")
    private String feeType;
    private static final BigDecimal NUM_100 = new BigDecimal(100);

    private Money() {
    }

    private Money(Integer totalFee, String feeType) {
        Preconditions.checkArgument(totalFee != null);
        Preconditions.checkArgument(StringUtils.isNotEmpty(feeType));
        Preconditions.checkArgument(totalFee.longValue() > 0);
        this.totalFee = totalFee;
        this.feeType = feeType;
    }

    public static Money apply(Integer totalFee){
        return apply(totalFee, DEFAULT_FEE_TYPE);
    }

    public static Money apply(Integer totalFee, String feeType){
        return new Money(totalFee, feeType);
    }


}
