package com.wmc.AutomateApiDocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.constraints.Null;
/**
 * 类是否被生成api文档
 * @author OP-T-PC-0036
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ApiDocsClass {

    /**
     * 请求对象
     */
	Class<?> value() default Null.class;
	
   
}
