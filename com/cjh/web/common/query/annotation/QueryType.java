package com.cjh.web.common.query.annotation;

public enum QueryType {
	EQUAL("equal"),
	LESS_THAN("lt"),
	LESS_THAN_EQUAL("le"),
	GREATEROR_THAN("gt"),
	GREATEROR_THAN_EQUAL("ge"),
	NOT_EQUAL("notEqual"), 
	IS_NULL("isNull"), 
	IS_NOT_NULL("isNotNull"), 
	FULL_LIKE("like"),
	LEFT_LIKE("like"),
	RIGHT_LIKE("like"),
	NOT_LIKE("notLike"), 
	IN("in");
	
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
