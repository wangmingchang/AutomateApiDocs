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
    /**序列化字段名称*/
    public static final String SERIAL_VERSION_VID = "serialVersionUID";

    /**类或者方法说明的开头数组*/
    public static final String[] EXPLAIN_ARR = {"@explain", "@Explain", "@description" ,"@Description"};
    /**类或者方法说明的开头数组*/
    public static final String[] NO_CHECK_ARR = {"@since"};
    /**类或者方法requestMapping*/
    public static final String[] SPRING_REQUEST_MAPPER = {"@RequestMapping", "@PostMapping", "@GetMapping"};
    /**字段作用域-private*/
    public static final String FIELD_SCOPE_PRIVATE = "private";
    /**字段作用域-public*/
    public static final String FIELD_SCOPE_PUBLIC = "public";
    /**字段作用域-class*/
    public static final String FIELD_SCOPE_CLASS = "class";
    /**字段作用域-interface*/
    public static final String FIELD_SCOPE_INTERFACE = "interface";


    /**多行注释开头标记*/
    public static String MORE_START_FLAG = "/**";
    /**多行注释结束标记*/
    public static String MORE_END_FLAG = "*/";
    /**多行注释星（*）标记*/
    public static String STAR_FLAG = "*";
    /**单行注释（//）标记*/
    public static String ONE_WAY_FLAG = "//";


}
