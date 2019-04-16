package com.cjh.web.common.query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static <T> T findOne(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
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
	
	@Deprecated
	@SuppressWarnings( { "rawtypes", "unused" } )
	private static <T> Page<T> findAllByPage(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
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
	
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unused" })
	private static <T> List<T> findAll(JpaSpecificationExecutor<?> dao, Object searchObj, MarryType marryType){
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
		Integer pageSize = QueryConditionFactory.getPageSize(searchObj, 10);
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getOneBy(JpaSpecificationExecutor<?> dao, String propertyName, Object value){
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		Optional<T> result =  (Optional<T>) dao.findOne(p);
		return (result.isPresent()) ? result.get() : null ;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, String propertyName, Object value){
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (List<T>) dao.findAll(p);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, String orderField,
			Direction direction) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		Sort sort = Sort.by(direction, orderField);
		return (List<T>) dao.findAll(p, sort);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> PageQueryBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, int currentPage,
			int pageSize) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> PageQueryBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, int currentPage,
			int pageSize, Sort sort) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize, sort));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> PageQuery(JpaSpecificationExecutor<?> dao,
			Map<String, Object> propertyMap,LinkedHashMap<String, Direction> sortMap, 
			Integer currentPage, Integer pageSize) {
		Specification p = QueryConditionFactory2.getSpecification(propertyMap);
		currentPage = currentPage == null ? 0 : currentPage;
		pageSize = pageSize == null ? 10 : pageSize;
		if(sortMap == null){
			return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize));
		}
		Sort sort = QueryConditionFactory2.getSort(sortMap);
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize, sort));
	}
	
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static Long count(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification spec = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		return dao.count(spec);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, Map<String,Object> propertyMap){
		Specification p = QueryConditionFactory2.getSpecification(propertyMap);
		return (List<T>) dao.findAll(p);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, Map<String,Object> propertyMap, LinkedHashMap<String,Direction> sortMap){
		if(sortMap == null){
			return getAllBy(dao, propertyMap);
		}
		Specification p = QueryConditionFactory2.getSpecification(propertyMap);
		Sort sort = QueryConditionFactory2.getSort(sortMap);
		return (List<T>) dao.findAll(p, sort);
	}
}
