package com.redwerk.likelabs.testutils;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

public class ReflectFieldExpose {
	private final Object object;
	
	public ReflectFieldExpose(Object object) {
		this.object = object;
	}
	
	public ReflectFieldExpose set(String fieldName, Object value) throws Exception {
		Field field = ReflectionUtils.findField(object.getClass(), fieldName);
		field.setAccessible(true);
		field.set(object, value);
		return this;		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String fieldName) throws Exception {
		Field field = ReflectionUtils.findField(object.getClass(), fieldName);
		field.setAccessible(true);
		return (T) field.get(object);
	}
}