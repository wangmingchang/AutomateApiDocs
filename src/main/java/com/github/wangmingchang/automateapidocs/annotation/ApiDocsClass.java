package com.github.wangmingchang.automateapidocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类是否被生成api文档
 * 
 * @author 王明昌
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ApiDocsClass {

	/**
	 * 请求对象
	 * @return 类的名称
	 */
	Class<?> value() default Null.class;

	final class Null {

	}
}
