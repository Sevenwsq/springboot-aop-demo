package cn.wsq.aopdemo.annotation;

import java.lang.annotation.*;

/**
 * @author Silent
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogAnnotation {
    String value() default "";
}
