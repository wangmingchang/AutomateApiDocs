package com.github.wangmingchang.automateapidocs.utils.apidocs;

/**
 * 常量util
 * @author wangmingchang
 * @data 2019\1\20 0020 20:43
 **/
public class ConstantsUtil {

    /**保存多行注释方法的Map的key*/
    public static final String METHOD_MAP_KEY = "methodMapKey";
    /**ApiDocsClass带@的字符串*/
    public static final String API_DOCS_CLASS_STR = "@ApiDocsClass";
    /**ApiDocsMethod@的字符串*/
    public static final String API_DOCS_METHOD = "@ApiDocsMethod";

    /**author的开关数组*/
    public static final String[] AUTHOR_ARR = {"@author", "@Author"};
    /**date的开头数组*/
    public static final String[] DATE_ARR = {"@date", "@Date"};
    /**param的开头*/
    public static final String PARAM_STR= "@param";
    /**return的开头*/
    public static final String RETURN_STR = "@return";

    /**类或者方法说明的开头数组*/
    public static final String[] EXPLAIN_ARR = {"@explain", "@Explain", "@description" ,"@Description"};


}
