package com.cjh.web.common.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

/**
 * 查询条件工厂,和注解查询无关
 * @author chen
 *
 */
public class QueryConditionFactory2 {
	
	/**
	 * 根据单个字段和值生成查询条件
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static <T> Specification<T> getSpecification(String propertyName, Object value){
		@SuppressWarnings("serial")
		Specification<T> s1 = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				if(propertyName == null || "".equals(propertyName)){
					return cb.conjunction();
				}
				return cb.equal(root.get(propertyName), value);
			}
		};
		return s1;
	}
	
	/**
	 * 根据map集合的字段名和值生成查询条件
	 * @param map
	 * @return
	 */
	public static <T> Specification<T> getSpecification(Map<String,Object> map){
		@SuppressWarnings("serial")
		Specification<T> s1 = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				if(map == null){
					return cb.conjunction();
				}
				List<Predicate> predicateList = new ArrayList<Predicate>();
				for(Map.Entry<String, Object> entry : map.entrySet())
		        {
					if(entry.getKey() != null && !"".equals(entry.getKey())){
						Predicate p = cb.equal(root.get(entry.getKey()), entry.getValue());
						predicateList.add(p);
					}
		        }
				Predicate[] t = new Predicate[predicateList.size()];
				predicateList.toArray(t);
				return cb.and(t);
			}
		};
		return s1;
	}
	
	/**
	 * 生成排序查询条件
	 * @param sortMap 排序map
	 * @return
	 */
	public static Sort getSort(Map<String,Direction> sortMap){
		if(sortMap == null){
			return null;
		}
		List<Order> orderList = new ArrayList<Order>();
		for (Map.Entry<String, Direction> entry : sortMap.entrySet()) {
			if(entry.getKey() != null && !"".equals(entry.getKey())){
				orderList.add(new Order(entry.getValue(), entry.getKey()));
			}
		}
		return Sort.by(orderList);
		
	}
}
