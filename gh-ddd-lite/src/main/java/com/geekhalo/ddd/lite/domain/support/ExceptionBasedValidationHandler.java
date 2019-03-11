package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.ValidationException;
import com.geekhalo.ddd.lite.domain.ValidationChecker;
import org.springframework.util.CollectionUtils;

/**
 * Created by taoli on 17/11/17.
 */
public class ExceptionBasedValidationHandler extends AbstractValidationHandler implements ValidationChecker {

    @Override
    public void check(){
        if (!CollectionUtils.isEmpty(getErrorMsg())){
            throw new ValidationException(getErrorMsg());
        }
    }
}
