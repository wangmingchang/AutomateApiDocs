package com.wmc.AutomateApiDocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求参数的是否必传
 * @author 王明昌
 * @date 2017年9月15日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ApiDocsParam {
	/**
	 * 字段是否是必传参数
	 * @return
	 */
	boolean value() default true;
}
