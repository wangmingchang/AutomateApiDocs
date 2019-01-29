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
    public static final String MORE_START_FLAG = "/**";
    /**多行注释结束标记*/
    public static final String MORE_END_FLAG = "*/";
    /**多行注释星（*）标记*/
    public static final String STAR_FLAG = "*";
    /**单行注释（//）标记*/
    public static final String ONE_WAY_FLAG = "//";

    /**保存备注value的key*/
    public static final String METHOD_DESCRIPTION_VALUE_KEY = "methodDescriptionValue";
    /**保存备注key的key*/
    public static final String METHOD_KEY = "methodKey";

    /**properties配置的controller*/
    public static final String PROPERTIES_CONTROLLER = "apiDocs.key.controller.";
    /**properties配置的entry*/
    public static final String PROPERTIES_ENTRY = "apiDocs.key.entry.";
    /**properties配置的reference*/
    public static final String PROPERTIES_REFERENCE = "apiDocs.key.reference.";
    /**properties配置的sys*/
    public static final String PROPERTIES_SYS = "apiDocs.sys.";
    /**properties配置的controller的方法前缀*/
    public static final String PROPERTIES_CONTROLLER_START = "${controller.";
    /**保存在Map的key*/
    public static final String PROPERTIES_CONTROLLER_METHOD_NAME = "methodNameKey";

    /**properties配置的method的方法信息的后缀*/
    public static final String APIDOCS_METHOD_URL = "ApiDocsMethod.url";
    public static final String APIDOCS_METHOD_TYP = "ApiDocsMethod.typ";
    public static final String APIDOCS_METHOD_METHOD_EXPLAIN = "ApiDocsMethod.methodExplain";
    public static final String APIDOCS_METHOD_REQUEST_BEAN = "ApiDocsMethod.requestBean";
    public static final String APIDOCS_METHOD_BASE_RESPONSE_BEAN = "ApiDocsMethod.baseResponseBean";
    public static final String APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY = "ApiDocsMethod.baseResponseBeanGenericity";
    public static final String APIDOCS_METHOD_RESPONSE_BEAN = "ApiDocsMethod.responseBean";
    public static final String APIDOCS_METHOD_RESPONSE_BEANS = "ApiDocsMethod.responseBeans";
    public static final String APIDOCS_PARAM_REQUEST_FALSE = "ApiDocsParam.request.false";
    public static final String APIDOCS_PARAM_REQUEST_TRUE = "ApiDocsParam.request.true";
    public static final String APIDOCS_PARAM_REQUEST_ISSHOW_FALSE = "ApiDocsParam.request.isShow.false";
    public static final String APIDOCS_PARAM_REQUEST_ISSHOW_TRUE = "ApiDocsParam.request.isShow.true";
    public static final String APIDOCS_PARAM_RESPONSE_ISSHOW_TRUE = "ApiDocsParam.response.isShow.true";
    public static final String APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE = "ApiDocsParam.response.isShow.false";


}
