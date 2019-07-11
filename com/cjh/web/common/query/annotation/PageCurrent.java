package com.cjh.web.common.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前页注解
 * @author chen
 *
 * 2019年7月11日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageCurrent {
	/** 当前页的默认值，默认为0 **/
	int value() default 0;
}
