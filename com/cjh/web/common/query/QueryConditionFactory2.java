package com.cjh.web.common.query;

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
}
