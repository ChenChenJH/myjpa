package com.cjh.web.common.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.cjh.web.common.query.annotation.PageCurrent;
import com.cjh.web.common.query.annotation.PageSize;
import com.cjh.web.common.query.annotation.QueryField;
import com.cjh.web.common.query.annotation.QueryGroup;
import com.cjh.web.common.query.annotation.QueryOrder;
import com.cjh.web.common.query.annotation.QueryType;
import java.util.Collection;

/**
 * 
 * @author chen
 *
 * 2019年1月8日
 */
public class QueryConditionFactory {
	
	/**
	 * 获取动态查询条件
	 * @param obj
	 * @param marryType
	 * @return
	 */
	public static <T> Specification<T> getSpecification(final Object obj,final MarryType marryType){
		//有@QueryGroup注解的字段
		final Field groupField = getFiledByAnnotation(obj, QueryGroup.class);
		
		@SuppressWarnings("serial")
		Specification<T> s1 = new Specification<T>() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				//动态查询条件
				List<Predicate> predicateList = getPredicates(cb, root, obj);
				Predicate[] t = new Predicate[predicateList.size()];
				predicateList.toArray(t);
				//动态分组条件
				if(groupField != null){
					List grouping = getGroups(root, obj, groupField);
					if(grouping != null){
						query.groupBy(grouping);
					}
				}
				
				return (marryType == MarryType.OR && predicateList.size() > 1) ? cb.or(t) : cb.and(t);
				
			}
		};
		return s1;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Predicate getPredicate(CriteriaBuilder cb,Root root, String fieldName, Object value, QueryType type){
		if (type == QueryType.EQUAL) {
			return cb.equal(getExpression(root,fieldName), value);
		} else if (type == QueryType.NOT_EQUAL) {
			return cb.notEqual(getExpression(root,fieldName), value);
		} else if (type == QueryType.FULL_LIKE) {
			return cb.like(cb.lower(getExpression(root,fieldName)),
					"%" + String.valueOf(value).toLowerCase() + "%");
		} else if (type == QueryType.LEFT_LIKE) {
			return cb.like(cb.lower(getExpression(root,fieldName)),
					"%" + String.valueOf(value).toLowerCase());
		} else if (type == QueryType.RIGHT_LIKE) {
			return cb.like(cb.lower(getExpression(root,fieldName)), String.valueOf(value)
					.toLowerCase() + "%");
		} else if (type == QueryType.NOT_LIKE) {
			return cb.notLike(getExpression(root,fieldName), "%" + String.valueOf(value)
					+ "%");
		} else if (type == QueryType.LESS_THAN) {
			return cb.lt(getExpression(root,fieldName),
					Integer.valueOf(String.valueOf(value)));
		} else if (type == QueryType.LESS_THAN_EQUAL) {
			return cb.le(getExpression(root,fieldName),
					Integer.valueOf(String.valueOf(value)));
		} else if (type == QueryType.GREATEROR_THAN) {
			return cb.gt(getExpression(root,fieldName),
					Integer.valueOf(String.valueOf(value)));
		} else if (type == QueryType.GREATEROR_THAN_EQUAL) {
			return cb.ge(getExpression(root,fieldName),
					Integer.valueOf(String.valueOf(value)));
		} else if (type == QueryType.IS_NULL) {
			return cb.isNull(getExpression(root,fieldName));
		} else if (type == QueryType.IS_NOT_NULL) {
			return cb.isNotNull(getExpression(root,fieldName));
		} else if (type == QueryType.IN){
			return root.get(fieldName).in(((Collection)value).toArray());
		}
		
		return null;
	}
	
	/**
	 * 根据字段获取排序
	 * @param orderby
	 * @return
	 */
	public static Sort getSort(String orderby){
		if(orderby == null || "".equals(orderby)){
			return null;
		}
		List<Order> orderList = new ArrayList<Order>();
		String[] orderGroup = orderby.split(";");
		for(String s : orderGroup){
			String[] strs = s.split(",");
			if(strs.length == 1){
				orderList.add(Order.by(strs[0]));
				Sort.by(strs[0]);
			}else{
				if("asc".equalsIgnoreCase(strs[1])){
					orderList.add(Order.by(strs[0]));
				}else if("desc".equalsIgnoreCase(strs[1])){
					orderList.add(new Order(Direction.DESC, strs[0]));
				}else{
					throw new RuntimeException("字段的排序方式有误!!!应该为---asc|ASC---或者desc|DESC");
				}
			}
		}
		return Sort.by(orderList);
	}
	
	/**
	 * 根据对象里面带有@QueryOrder注解的字段获取排序
	 * @param obj
	 * @return
	 */
	public static Sort getSort(Object obj){
		Field field = getFiledByAnnotation(obj, QueryOrder.class);
		if(field == null){
			return null;
		}
		try {
			Method m = obj.getClass().getMethod("get" + initial(field.getName()));
			Object value = m.invoke(obj);
			if(value == null || "".equals(value)){
				return null;
			}
			return getSort(String.valueOf(value));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 根据对象和分组的字段,获取分组的集合
	 * @param obj
	 * @param field
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Expression> getGroups(Root root, Object obj, Field field){
		List<Expression> groups= new ArrayList<>();
		Method m;
		try {
			m = obj.getClass().getMethod("get" + initial(field.getName()));
			Object value = m.invoke(obj);
			if(value != null && !"".equals(value)){
				String[] strs = String.valueOf(value).split(",");
				for(String str : strs){
					groups.add(getExpression(root, str));
				}
				return groups;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}
	
	/**
	 * 根据对象里面的@PageCurrent获取当前页的值
	 * @param obj
	 * @param defaultValue 如果对象里面没有该注解，则返回这个值
	 * @return
	 */
	public static Integer getCurrentPage(Object obj, Integer defaultValue){
		Field field = getFiledByAnnotation(obj, PageCurrent.class);
		if(field == null){
			return defaultValue;
		}
		try {
			Method m = obj.getClass().getMethod("get" + initial(field.getName()));
			Object value = m.invoke(obj);
			//如果字段有值，直接返回值,否则返回注解的默认值
			if(value != null && !"".equals(value)){
				int pageSize = Integer.valueOf(String.valueOf(value));
				if(pageSize < 1){
					return defaultValue;
				}else{
					return pageSize;
				}
			}
			return field.getAnnotation(PageCurrent.class).value();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 根据对象里面的@PageSize获取每页显示数量值
	 * @param obj
	 * @param defaultValue 如果对象里面没有该注解，则返回这个值
	 * @return
	 */
	public static Integer getPageSize(Object obj, Integer defaultValue){
		Field field = getFiledByAnnotation(obj, PageSize.class);
		if(field == null){
			return defaultValue;
		}
		try {
			Method m = obj.getClass().getMethod("get" + initial(field.getName()));
			Object value = m.invoke(obj);
			//如果字段有值，直接返回值,否则返回注解的默认值
			if(value != null && !"".equals(value)){
				int pageSize = Integer.valueOf(String.valueOf(value));
				if(pageSize < 1){
					return defaultValue;
				}else{
					return pageSize;
				}
			}
			return field.getAnnotation(PageSize.class).value();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询条件之间的连接符,例如where id = ? AND num = ?;where id = ? OR num = ?
	 * @author chen
	 *
	 * 2019年1月8日
	 */
	public static enum MarryType{
		AND, OR
	}
	
	/**
	 * 获取有@QueryField注解字段的动态条件集合
	 * @param cb
	 * @param root
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static List<Predicate> getPredicates(CriteriaBuilder cb,Root root, Object obj){
		List<Predicate> predicateList = new ArrayList<Predicate>();
		try {
			Field[] fields1 = obj.getClass().getDeclaredFields();
			Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();
			Field[] fields = new Field[fields1.length + fields2.length];
			System.arraycopy(fields1,0,fields,0,fields1.length);
			System.arraycopy(fields2,0,fields,fields1.length,fields2.length);
			fields1 = null;
			fields2 = null;
			Method m;
			Object value;
			for(Field f : fields ){
				QueryField annotation = f.getAnnotation(QueryField.class);
				if(annotation != null){
					//对应查询实体类的属性名称
					String queryName = annotation.name();
					if("".equals(queryName)){
						queryName = f.getName();
					}
					m = obj.getClass().getMethod("get" + initial(f.getName()));
					//反射执行方法获取值
					value = m.invoke(obj);
					if(value!= null && !"".equals(value)){
						Predicate p = getPredicate(cb, root, queryName, value, annotation.type());
						predicateList.add(p);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return predicateList;
		
	}
	
	/**
	 * 返回首字母大写的字符串
	 * @param str
	 * @return
	 */
	private static String initial(String str){
		return str.substring(0,1).toUpperCase().concat(str.substring(1));
	}
	
	/**
	 * 根据名称导航到root最底级属性，例如:person.id
	 * @param root
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Expression getExpression(Root root,String fieldName){
		if(fieldName.indexOf(".") != -1){
			Path path = null;
			int index = 0;
			for(String name : fieldName.split("\\.")){
				if(index < 1){
					path = root.get(name);
					index++;
				}else{
					path = path.get(name);
				}
			}
			return path;
		}
		return root.get(fieldName);
	}
	
	/**
	 * 返回对象中一个有annotationClass注解类的字段
	 * @param obj 对象
	 * @param annotationClass 注解类
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private static Field getFiledByAnnotation(Object obj,Class annotationClass){
		return findField(obj.getClass(), annotationClass);
	}
	
	/**
	 * 从该类开始往父类查找有该注解类的一个字段(找到即停止查找)，并返回
	 * @param currentClass
	 * @param annotationClass 注解类
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Field findField(Class<?> currentClass,Class annotationClass){
		if(currentClass == null || currentClass == Object.class){
			return null;
		}
		Object annotation = null;
		for(Field f : currentClass.getDeclaredFields() ){
			annotation = f.getAnnotation(annotationClass);
			//查询到有这个注解，则直接返回该字段
			if(annotation != null){
				return f;
			}
		}
		return findField(currentClass.getSuperclass(), annotationClass);
	}
	
}
