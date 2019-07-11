package com.cjh.web.common.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分组条件注解,生成分组条件,标识在String字段上
 * 用法如下:
 * 单个: createDate
 * 多个用,隔开: createDate,keyid
 * 2019年7月11日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryGroup {
}
