package com.geekhalo.ddd.lite.domain.support.bitopt;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/4/6.
 */
@Data
public class IntMaskOp implements IntBitOp{
    private final int mask;

    private static  List<IntMaskOp> maskOps;


    private IntMaskOp(int mask) {
        this.mask = mask;
    }

    public boolean isSet(int code){
        return (code & this.mask) == this.mask;
    }

    @Override
    public boolean match(int value) {
        return isSet(value);
    }

    public int set(int value, boolean isSet){
        if (isSet){
            return set(value);
        }else {
            return unset(value);
        }
    }

    public int set(int value){
        return value | this.mask;
    }

    public int unset(int value){
        return value & ~this.mask;
    }

    public static List<IntMaskOp> transform(int vaule){
        return maskOps.stream().filter(intMaskOp -> intMaskOp.match(vaule)).collect(Collectors.toList());
    }

    public static final IntMaskOp MASK_1 = new IntMaskOp(1 << 0);
    public static final IntMaskOp MASK_2 = new IntMaskOp(1 << 1);
    public static final IntMaskOp MASK_3 = new IntMaskOp(1 << 2);
    public static final IntMaskOp MASK_4 = new IntMaskOp(1 << 3);
    public static final IntMaskOp MASK_5 = new IntMaskOp(1 << 4);
    public static final IntMaskOp MASK_6 = new IntMaskOp(1 << 5);
    public static final IntMaskOp MASK_7 = new IntMaskOp(1 << 6);
    public static final IntMaskOp MASK_8 = new IntMaskOp(1 << 7);
    public static final IntMaskOp MASK_9 = new IntMaskOp(1 << 8);
    public static final IntMaskOp MASK_10 = new IntMaskOp(1 << 9);
    public static final IntMaskOp MASK_11 = new IntMaskOp(1 << 10);
    public static final IntMaskOp MASK_12 = new IntMaskOp(1 << 11);
    public static final IntMaskOp MASK_13 = new IntMaskOp(1 << 12);
    public static final IntMaskOp MASK_14 = new IntMaskOp(1 << 13);
    public static final IntMaskOp MASK_15 = new IntMaskOp(1 << 14);
    public static final IntMaskOp MASK_16 = new IntMaskOp(1 << 15);
    public static final IntMaskOp MASK_17 = new IntMaskOp(1 << 16);
    public static final IntMaskOp MASK_18 = new IntMaskOp(1 << 17);
    public static final IntMaskOp MASK_19 = new IntMaskOp(1 << 18);
    public static final IntMaskOp MASK_20 = new IntMaskOp(1 << 19);
    public static final IntMaskOp MASK_21 = new IntMaskOp(1 << 20);
    public static final IntMaskOp MASK_22 = new IntMaskOp(1 << 21);
    public static final IntMaskOp MASK_23 = new IntMaskOp(1 << 22);
    public static final IntMaskOp MASK_24 = new IntMaskOp(1 << 23);
    public static final IntMaskOp MASK_25 = new IntMaskOp(1 << 24);
    public static final IntMaskOp MASK_26 = new IntMaskOp(1 << 25);
    public static final IntMaskOp MASK_27 = new IntMaskOp(1 << 26);
    public static final IntMaskOp MASK_28 = new IntMaskOp(1 << 27);
    public static final IntMaskOp MASK_29 = new IntMaskOp(1 << 28);
    public static final IntMaskOp MASK_30 = new IntMaskOp(1 << 29);
    public static final IntMaskOp MASK_31 = new IntMaskOp(1 << 30);
    public static final IntMaskOp MASK_32 = new IntMaskOp(1 << 31);


    static {
        maskOps = Arrays.asList(IntMaskOp.MASK_1,IntMaskOp.MASK_2,IntMaskOp.MASK_3,IntMaskOp.MASK_4,IntMaskOp.MASK_5,IntMaskOp.MASK_6,IntMaskOp.MASK_7,IntMaskOp.MASK_8,IntMaskOp.MASK_9,IntMaskOp.MASK_10,
                IntMaskOp.MASK_11,IntMaskOp.MASK_12,IntMaskOp.MASK_13,IntMaskOp.MASK_14,IntMaskOp.MASK_15,IntMaskOp.MASK_16,IntMaskOp.MASK_17,IntMaskOp.MASK_18,IntMaskOp.MASK_19,IntMaskOp.MASK_20,
                IntMaskOp.MASK_21,IntMaskOp.MASK_22,IntMaskOp.MASK_23,IntMaskOp.MASK_24,IntMaskOp.MASK_25,IntMaskOp.MASK_26,IntMaskOp.MASK_27,IntMaskOp.MASK_28,IntMaskOp.MASK_29,IntMaskOp.MASK_30,
                IntMaskOp.MASK_31,IntMaskOp.MASK_32);
    }

}
