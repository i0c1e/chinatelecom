package com.victor.chinatelecom.common.API;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetRef {
    String value();
}
