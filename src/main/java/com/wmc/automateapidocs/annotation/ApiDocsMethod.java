package com.wmc.automateapidocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 该方法是否要生成api
 * @author 王明昌
 * @date 2017年10月17日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiDocsMethod {

    /**
     * 请求对象
     */
	Class<?> requestBean() default Null.class;
	
    /**
     * 响应对象(返回的基础对象如：BaseRespons)
     */
	Class<?> baseResponseBean() default Null.class;
	
    /**
     * 响应对象
     */
	Class<?> responseBean() default Null.class;
	/**
	 * 多个返回对象，以数组方式传入
	 * @return
	 */
	Class<?>[] responseBeans() default {};
    /**
     * 请求url
     */
	String url() default "";

    /**
     * 请求方式
     */
	String type() default "post";
	/**
	 * 方法说明
	 * @return
	 */
	String methodExplain() default "";

    final class Null{

    }
}
