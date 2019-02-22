package com.geekhalo.ddd.lite.domain.support;

import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class SelfDescribedEnumVo {

    private final String key;
    private final String desc;

    public static <E extends Enum & SelfDescribedEnum> SelfDescribedEnumVo apply(E describedEnum) {
        if (describedEnum == null || describedEnum.name().equals("NONE")) {
            return null;
        }
        return new SelfDescribedEnumVo(describedEnum.name(), describedEnum.getDescription());
    }

    public static <E extends Enum & SelfDescribedEnum> List<SelfDescribedEnumVo> apply(Class<E> eClass) {
        return Arrays.asList(eClass.getEnumConstants()).stream()
                .filter(e -> !e.name().equals("NONE"))
                .map(SelfDescribedEnumVo::apply)
                .filter(selfDescribedEnumVo -> selfDescribedEnumVo != null)
                .collect(Collectors.toList());
    }
}
