package com.cjh.web.common.query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.cjh.web.common.query.QueryConditionFactory.MarryType;

/**
 * 查询工具类,find开关的方法均是使用注解条件查询
 * @author chen
 *
 * 2019年1月9日
 */
@Component
public class QueryUtil {
	
	private static EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}

	@Autowired
	public void setEm(EntityManager em) {
		QueryUtil.em = em;
	}

	/**
	 * 根据条件类查询一条记录,没有则返回null
	 * @param dao
	 * @param searchObj 条件类
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T findOne(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		Optional<T> result =  (Optional<T>) dao.findOne(p);
		return (result.isPresent()) ? result.get() : null ;
	}
	
	/**
	 * 根据条件类进行分页查询
	 * @param dao
	 * @param searchObj 条件类
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <T> Page<T> findAllByPage(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		return pageQuery(dao, searchObj, p);
	}
	
	/**
	 * 根据条件类进行列表查询
	 * @param dao
	 * @param searchObj 条件类
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <T> List<T> findAll(JpaSpecificationExecutor<?> dao, Object searchObj){
		Specification p = QueryConditionFactory.getSpecification(searchObj, MarryType.AND);
		return listQuery(dao, searchObj, p);
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
	
	/**
	 * 根据属性值查询一条数据，没有返回null
	 * @param dao
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getOneBy(JpaSpecificationExecutor<?> dao, String propertyName, Object value){
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		Optional<T> result =  (Optional<T>) dao.findOne(p);
		return (result.isPresent()) ? result.get() : null ;
	}
	
	/**
	 * 根据属性查询多条数据
	 * @param dao
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, String propertyName, Object value){
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (List<T>) dao.findAll(p);
	}
	
	/**
	 * 根据属性和排序值查询多条数据
	 * @param dao
	 * @param propertyName 属性名
	 * @param value	属性值
	 * @param orderField 排序属性
	 * @param direction 排序方式
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, String orderField,
			Direction direction) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		Sort sort = Sort.by(direction, orderField);
		return (List<T>) dao.findAll(p, sort);
	}
	
	/**
	 * 根据一个属性条件进行分页查询
	 * @param dao
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param currentPage 当前页
	 * @param pageSize 每页数据量
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> PageQueryBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, int currentPage,
			int pageSize) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize));
	}
	
	/**
	 * 根据一个属性条件进行分页和排序查询
	 * @param dao
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param currentPage 当前
	 * @param pageSize 每页数据量
	 * @param sort 排序值
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> PageQueryBy(JpaSpecificationExecutor<?> dao,
			String propertyName, Object value, int currentPage,
			int pageSize, Sort sort) {
		Specification p = QueryConditionFactory2.getSpecification(propertyName, value);
		return (Page<T>) dao.findAll(p, PageRequest.of(currentPage, pageSize, sort));
	}
	
	/**
	 * 根据属性条件集合和排序集合进行分页查询
	 * @param dao
	 * @param propertyMap 属性集合
	 * @param sortMap 排序集合
	 * @param currentPage 当前页
	 * @param pageSize 每页数据量
	 * @return
	 */
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
	
	/**
	 * 根据查询条件map进行列表查询
	 * @param dao
	 * @param propertyMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, Map<String,Object> propertyMap){
		Specification p = QueryConditionFactory2.getSpecification(propertyMap);
		return (List<T>) dao.findAll(p);
	}
	
	/**
	 * 根据查询条件map进行列表查询[带排序]
	 * @param dao
	 * @param propertyMap 属性条件map
	 * @param sortMap 排序条件map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAllBy(JpaSpecificationExecutor<?> dao, Map<String,Object> propertyMap, LinkedHashMap<String,Direction> sortMap){
		if(sortMap == null){
			return getAllBy(dao, propertyMap);
		}
		Specification p = QueryConditionFactory2.getSpecification(propertyMap);
		Sort sort = QueryConditionFactory2.getSort(sortMap);
		return (List<T>) dao.findAll(p, sort);
	}
	
	/**
	 * 执行除了查询语句的hql
	 * @param hql
	 * @param values
	 * @return 影响的条数
	 */
	public static int executeHql(String hql,Object... values){
		Query query = em.createQuery(hql);
		if (values != null) {
			for (int i = 1; i <= values.length; i++) {
				query.setParameter(i, values[i-1]);
			}
		}
		return query.executeUpdate();
	}
	
}
