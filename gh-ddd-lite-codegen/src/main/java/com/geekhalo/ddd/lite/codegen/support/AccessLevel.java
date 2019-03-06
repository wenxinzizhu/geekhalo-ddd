package com.geekhalo.ddd.lite.codegen.support;


import javax.lang.model.element.Modifier;
import java.util.Set;

public enum AccessLevel {
    PUBLIC,
    PROTECTED,
    PACKAGE,
    PRIVATE;

    public static AccessLevel getFromModifers(Set<Modifier> modifiers){
        if (modifiers.contains(Modifier.PUBLIC)){
            return PUBLIC;
        }
        if (modifiers.contains(Modifier.PRIVATE)){
            return PRIVATE;
        }
        if (modifiers.contains(Modifier.PROTECTED)){
            return PROTECTED;
        }
        return PACKAGE;
    }

    public static AccessLevel getFromAccessLecel(lombok.AccessLevel accessLevel){
        switch (accessLevel){
            case PUBLIC:
                return PUBLIC;
            case PROTECTED:
                return PROTECTED;
            case PRIVATE:
                return PRIVATE;
        }
        return PACKAGE;
    }
}
