package com.wmc.AutomateApiDocs;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnnotationTest {  

	public static void main(String[] args) throws Exception {
		Class<?> apply = Class.forName("com.wmc.AutomateApiDocs.pojo.vo.DemoVo");
		System.out.println(apply);
		Field[] fields = apply.getDeclaredFields();
		//获取所有属性
		List<List<Field>> allFieldList = getBomFields1(new ArrayList<Field>(), fields);
		for (List<Field> list : allFieldList) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
			System.out.println(list.toString());
		}
	}

	protected static List<List<Field>> getBomFields(List<Field> chain, Field[] fields) {
		List<List<Field>> result = new ArrayList<List<Field>>();
		for (Field field : fields) {
			Class<?> fieldClass = field.getType();
			if (fieldClass.getName().startsWith("java")
					|| fieldClass.getName().startsWith("javax")
					|| fieldClass.getName().startsWith("com.sun")
					|| fieldClass.getName().startsWith("boolean")
					|| fieldClass.getName().startsWith("double")
					|| fieldClass.getName().startsWith("int")) {
				List<Field> endChain = new ArrayList<Field>(chain);
				endChain.add(field);
				result.add(endChain);
				continue;
			} else {
				List<Field> thisChain = new ArrayList<Field>(chain);
				thisChain.add(field);
				result.addAll(getBomFields(new java.util.ArrayList<Field>(
						thisChain), fieldClass.getDeclaredFields()));
			}
		}
		return result;
	}
	public static List<List<Field>> getBomFields1(List<Field> chain,
			Field[] fields) {
		List<List<Field>> result = new ArrayList<List<Field>>();
		for (Field field : fields) {
			Class<?> fieldClass = field.getType();
			if (fieldClass.isPrimitive()
					|| fieldClass.getName().startsWith("java.lang")
					|| fieldClass.getName().startsWith("java.util.Date")
					|| fieldClass.getName().startsWith("javax")
					|| fieldClass.getName().startsWith("com.sun")
					|| fieldClass.getName().startsWith("sun")
					|| fieldClass.getName().startsWith("boolean")
					|| fieldClass.getName().startsWith("double")
					|| fieldClass.getName().startsWith("int")) {
				List<Field> endChain = new ArrayList<Field>(chain);
				endChain.add(field);
				result.add(endChain);
				continue;
			} else {
				if (fieldClass.isAssignableFrom(List.class)) // 【2】
				{
					Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
					if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
					{
						ParameterizedType pt = (ParameterizedType) fc;
						Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // 【4】
						if ( genericClazz.getName().startsWith("java.lang") //设置list的终止类型
								|| genericClazz.getName().startsWith("java.util.Date")
								|| genericClazz.getName().startsWith("javax")
								|| genericClazz.getName().startsWith("com.sun")
								|| genericClazz.getName().startsWith("sun")
								|| genericClazz.getName().startsWith("boolean")
								|| genericClazz.getName().startsWith("double")
								|| genericClazz.getName().startsWith("int")) {
							continue;
						}
						//System.out.println(genericClazz);
																// 得到泛型里的class类型对象。
						List<Field> thisChain = new ArrayList<Field>(chain); 
//						System.out.println(chain);
						thisChain.add(field); //!!
						result.addAll(getBomFields1(new ArrayList<Field>(thisChain), genericClazz.getDeclaredFields()));
					}
				} else {
					List<Field> thisChain = new ArrayList<Field>(chain);
					thisChain.add(field);
					result.addAll(getBomFields1(new ArrayList<Field>(thisChain),
							fieldClass.getDeclaredFields()));
				}

			}
		}
		return result;
	}
}
