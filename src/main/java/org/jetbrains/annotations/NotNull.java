package org.jetbrains.annotations;

import java.lang.annotation.*;

@Documented
@Retention(value = RetentionPolicy.CLASS)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
public @interface NotNull {
    public String value() default "";

    public Class<? extends Exception> exception() default Exception.class;
}
