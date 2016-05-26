package com.hfjy.mongoTest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hfjy.mongoTest.utils.StringUtils;


public class Condition implements Cloneable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5550812729851743045L;
	
	private List<Condition> conditions;
	private String key;
	private OperationType type;
	private Object value;
	
	private Condition(){
		
	}
	public static Condition init(){
		Condition con = new Condition();
		con.conditions = new ArrayList<>();
		return con;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public String getKey() {
		return key;
	}
	public OperationType getType() {
		return type;
	}
	public Object getValue() {
		return value;
	} 
	public Condition is(String key, Object value) {
		return addCondition(key, value, OperationType.EQUAL);
	}

	public Condition notIs(String key, Object value) {
		return addCondition(key, value, OperationType.NOT_EQUAL);
	}

	public Condition gt(String key, Object value) {
		return addCondition(key, value, OperationType.GREATER_THAN);
	}

	public Condition gte(String key, Object value) {
		return addCondition(key, value, OperationType.GREATER_THAN_EQUAL);
	}

	public Condition lt(String key, Object value) {
		return addCondition(key, value, OperationType.LESS_THAN);
	}

	public Condition lte(String key, Object value) {
		return addCondition(key, value, OperationType.LESS_THAN_EQUAL);
	}

	public Condition like(String key, String regex) {
		return addCondition(key, regex, OperationType.LIKE);
	}

	public Condition in(String key, Object... values) {
		return addCondition(key, values, OperationType.IN);
	}

	public Condition in(String key, Iterable<Object> values) {
		return addCondition(key, values, OperationType.IN);
	}

	public Condition notIn(String key, Object... values) {
		return addCondition(key, values, OperationType.NOT_IN);
	}

	public Condition notIn(String key, Iterable<Object> values) {
		return addCondition(key, values, OperationType.NOT_IN);
	}

	public Condition or(Condition condition) {
		try {
			Condition clone = condition.clone();
			clone.type = OperationType.OR;
			conditions.add(clone);
		} catch (Exception e) {
		}
		return this;
	}

	public Condition not(Condition condition) {
		try {
			Condition clone = condition.clone();
			clone.type = OperationType.NOT;
			conditions.add(clone);
		} catch (Exception e) {
		}
		return this;
	}

	
	
	private Condition addCondition(String key, Object value, OperationType type) {
		Condition condition = new Condition();
		condition.key = key;
		condition.type = type;
		condition.value = value;
		condition.conditions = conditions;
		conditions.add(condition);
		return this;
	}
	@Override
	public String toString() {
		return StringUtils.unite(key, " [", type, "] ", value);
	}
	
	@Override
	protected Condition clone() throws CloneNotSupportedException {
		Condition condition = Condition.init();
		condition.conditions.addAll(conditions);
		condition.key = key;
		condition.type = type;
		condition.value = value;
		return condition;
	}
}
