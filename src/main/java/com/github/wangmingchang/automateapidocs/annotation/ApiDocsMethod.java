package com.github.wangmingchang.automateapidocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 该方法是否要生成api
 * @author 王明昌
 * @since 2017年10月17日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiDocsMethod {

    /**
     * 请求对象
     * @return 请求对象的class名称
     */
	Class<?> requestBean() default Null.class;
	
    /**
     * 响应对象(返回的基础对象如：BaseRespons)
     * @return 返回基础对象的class名称
     */
	Class<?> baseResponseBean() default Null.class;

	/**
	 * 返回对象带有泛型的，真正对象
	 * @author wangmingchang
	 * @date 2019/1/18 11:22
	 * @return  返回对象的class名称
	 **/
	Class<?> baseResponseBeanGenericity() default Null.class;

    /**
     * 响应对象
     * @return 返回对象的class名称
     */
	Class<?> responseBean() default Null.class;

	/**
	 * 多个返回对象，以数组方式传入
	 * @return 返回多个对象的class名称数组
	 */
	Class<?>[] responseBeans() default {};

    /**
     * 请求url
     * @return 返回请求的url
     */
	String url() default "";

    /**
     * 请求方式
     * @return 返回请求方式
     */
	String type() default "post";
	/**
	 * 方法说明
	 * @return 返回方法说明
	 */
	String methodExplain() default "";

    final class Null{

    }
}
