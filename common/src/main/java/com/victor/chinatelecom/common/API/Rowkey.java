package com.victor.chinatelecom.common.API;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rowkey {


}
