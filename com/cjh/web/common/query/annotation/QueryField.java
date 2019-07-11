package com.cjh.web.common.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询字段注解，标识用此字段作为查询条件
 * @author chen
 *
 * 2019年7月11日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryField {
	/** 查询方式,默认是等于 **/
	QueryType type() default QueryType.EQUAL;
	
	/** 要对应的查询对象的属性名称,默认值是加注解的字段名称 **/
	String name() default "";
}
