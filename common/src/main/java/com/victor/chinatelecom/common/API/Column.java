/**
 * @author Charles
 * @create 2019/3/31
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.API;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String family() default "info";
    String column() default "";
}
