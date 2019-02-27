package com.geekhalo.ddd.lite.domain.support.vo;

import com.geekhalo.ddd.lite.domain.ValueObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@Embeddable
public class Money implements ValueObject {
    public static final String DEFAULT_FEE_TYPE = "CNY";
    @Column(name = "total_fee")
    private Long totalFee;
    @Column(name = "fee_type")
    private String feeType;
    private static final BigDecimal NUM_100 = new BigDecimal(100);

    private Money() {
    }

    private Money(Long totalFee, String feeType) {
        Preconditions.checkArgument(totalFee != null);
        Preconditions.checkArgument(StringUtils.isNotEmpty(feeType));
        Preconditions.checkArgument(totalFee.longValue() > 0);
        this.totalFee = totalFee;
        this.feeType = feeType;
    }

    public static Money apply(Long totalFee){
        return apply(totalFee, DEFAULT_FEE_TYPE);
    }

    public static Money apply(Long totalFee, String feeType){
        return new Money(totalFee, feeType);
    }


    public Money add(Money money){
        checkInput(money);
        return Money.apply(this.getTotalFee() + money.getTotalFee(), getFeeType());
    }

    private void checkInput(Money money) {
        if (money == null){
            throw new IllegalArgumentException("input money can not be null");
        }
        if (!this.getFeeType().equals(money.getFeeType())){
            throw new IllegalArgumentException("must be same fee type");
        }
    }

    public Money subtract(Money money){
        checkInput(money);
        if (getTotalFee() < money.getTotalFee()){
            throw new IllegalArgumentException("money can not be minus");
        }
        return Money.apply(this.getTotalFee() - money.getTotalFee(), this.getFeeType());
    }

    public Money multiply(int var){
        return Money.apply(this.getTotalFee() * var, getFeeType());
    }

    public List<Money> split(int count){
        if (getTotalFee() < count){
            throw new IllegalArgumentException("total fee can not lt count");
        }
        List<Money> result = Lists.newArrayList();
        Long pre = getTotalFee() / count;
        for (int i=0; i< count; i++){
            if (i == count-1){
                Long fee = getTotalFee() - (pre * (count - 1));
                result.add(Money.apply(fee, getFeeType()));
            }else {
                result.add(Money.apply(pre, getFeeType()));
            }
        }
        return result;
    }
}
