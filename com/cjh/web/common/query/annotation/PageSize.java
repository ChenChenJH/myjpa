package com.cjh.web.common.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 每页数量注解
 * @author chen
 *
 * 2019年7月11日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageSize {
	/** 每页数量的默认值,默认为10 **/
	int value() default 10;
}
