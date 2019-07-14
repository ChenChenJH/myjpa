package com.cjh.web.common.query.annotation;

/**
 * 查询方式，是等于还是大于还是别的啥啥....
 * @author chen
 *
 * 2019年7月11日
 */
public enum QueryType {
	/** 等于 **/
	EQUAL("equal"),
	/** 小于 **/
	LESS_THAN("lt"),
	/** 小于等于 **/
	LESS_THAN_EQUAL("le"),
	/** 大于 **/
	GREATEROR_THAN("gt"),
	/** 大于等于 **/
	GREATEROR_THAN_EQUAL("ge"),
	/** 不等于 **/
	NOT_EQUAL("notEqual"), 
	/** 为空 **/
	IS_NULL("isNull"), 
	/** 不为空 **/
	IS_NOT_NULL("isNotNull"), 
	/** 全模糊 **/
	FULL_LIKE("like"),
	/** 左模糊 **/
	LEFT_LIKE("leftLike"),
	/** 右模糊 **/
	RIGHT_LIKE("rightLike"),
	/** 不包括，全模糊 **/
	NOT_LIKE("notLike"), 
	/** 子查询，用于查询多个同一字段的值,例如同时查询id值为1,2,3的数据，加在实现了Collection接口的集合字段上 **/
	IN("in"),
	NOT_IN("notIn");
	
	private String operation;
	
	private QueryType(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
