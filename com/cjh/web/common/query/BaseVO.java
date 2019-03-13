package com.cjh.web.common.query;

import com.cjh.web.common.query.annotation.PageCurrent;
import com.cjh.web.common.query.annotation.PageSize;
import com.cjh.web.common.query.annotation.QueryGroup;
import com.cjh.web.common.query.annotation.QueryOrder;

/**
 * 查询Vo底层基类
 * @author chen
 *
 * 2019年1月11日
 */
public class BaseVO {
	@PageCurrent
	private Integer currentPage;
	@PageSize
	private Integer pageSize;
	@QueryOrder
	private String orderby;
	@QueryGroup
	private String groupby;
	private Long total;
	
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

}
