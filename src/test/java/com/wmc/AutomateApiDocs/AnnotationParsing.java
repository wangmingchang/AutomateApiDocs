package com.wmc.AutomateApiDocs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.wmc.AutomateApiDocs.utils.apidocs.ApiDocsUtil;

public class AnnotationParsing {

	private static final String ResponseDataDto = null;

	public static void getFruitInfo(String className) {
		try {
			Class<?> forName = Class.forName(className);
			forName.getDeclaredFields();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void getClassFieldAndMethod(Class cur_class) {
		String class_name = cur_class.getName();
		Field[] obj_fields = cur_class.getDeclaredFields();
		for (Field field : obj_fields) {
			field.setAccessible(true);
			System.out.println(class_name + ":" + field.getName());
		}
		Method[] methods = cur_class.getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			System.out.println(class_name + ":" + method.getName());
		}
		if (cur_class.getSuperclass() != null) {
			getClassFieldAndMethod(cur_class.getSuperclass());
		} 
	}

	public static void main(String[] args) {
		ApiDocsUtil.init();

	}
	
	

}
