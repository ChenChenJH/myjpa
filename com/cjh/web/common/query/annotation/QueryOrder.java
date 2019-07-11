package com.cjh.web.common.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序条件注解,生成排序条件,标识在String字段上
 * 用法如下:
 * 单个: createDate,asc 或者 createdate,desc
 * 多个则用;隔开: createDate,asc;keyid,desc
 * 注意:asc和desc不区分大小写
 * @author chen
 * 2019年7月11日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryOrder {
}
