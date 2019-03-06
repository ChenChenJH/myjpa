package com.cjh.web.common.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjh.web.common.query.QueryConditionFactory.MarryType;

/**
 * 
 * @author chen
 *
 * 2019年1月9日
 */
public class QueryUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T findOne(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		Optional<T> result =  (Optional<T>) dao.findOne(p);
		return (result.isPresent()) ? result.get() : null ;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T findOne(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
		if(marryType == MarryType.OR){
			Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.OR);
			return (T) dao.findOne(p);
		}
		return findOne(dao, searchObj);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static <T> Page<T> findAllByPage(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		return pageQuery(dao, searchObj, p);
	}
	
	@SuppressWarnings( { "rawtypes" } )
	public static <T> Page<T> findAllByPage(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
		if(marryType == MarryType.OR){
			Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.OR);
			return pageQuery(dao, searchObj, p);
		}
		return findAllByPage(dao, searchObj);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static <T> List<T> findAll(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		return listQuery(dao, searchObj, p);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static <T> List<T> findAll(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
		if(marryType == MarryType.OR){
			Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.OR);
			return listQuery(dao, searchObj, p);
		}
		return findAll(dao, searchObj);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Page<T> pageQuery(JpaSpecificationExecutor<?> dao, Object searchObj, Specification p){
		Sort sort = QueryConditionFactory.getSort(searchObj);
		Integer currentPage = QueryConditionFactory.getCurrentPage(searchObj, 0);
		Integer pageSize = QueryConditionFactory.getPageSize(searchObj, 20);
		if(sort == null){
			return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize));
		}
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize, sort));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> List<T> listQuery(JpaSpecificationExecutor<?> dao, Object searchObj, Specification p){
		Sort sort = QueryConditionFactory.getSort(searchObj);
		return (sort == null) ? (List<T>) dao.findAll(p) : (List<T>) dao
				.findAll(p, sort);
	}
}
