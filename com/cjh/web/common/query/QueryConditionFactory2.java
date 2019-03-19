package com.cjh.web.common.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * 查询条件工厂,和注解查询无关
 * @author chen
 *
 */
public class QueryConditionFactory2 {
	public static <T> Specification<T> getSpecification(String propertyName, Object value){
		@SuppressWarnings("serial")
		Specification<T> s1 = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				return cb.equal(root.get(propertyName), value);
			}
		};
		return s1;
	}
	
	public static <T> Specification<T> getSpecification(Map<String,Object> map){
		@SuppressWarnings("serial")
		Specification<T> s1 = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
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
}
