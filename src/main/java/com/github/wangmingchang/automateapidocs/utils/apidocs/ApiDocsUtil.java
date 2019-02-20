package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.*;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 自动生成API工具
 *
 * @author 王明昌
 */
public class ApiDocsUtil {
    private static boolean isWord; // 是否生成word格式api;
    private static boolean isHTML; // 是否生成HTML格式api;

    private static int sequence = 0; // 顺序号
    private static List<WordContentDto> wordContentDtos = new ArrayList<WordContentDto>();// word文档返回list
    private static List<ClassExplainDto> classExplains = new ArrayList<ClassExplainDto>(); // 类的业务说明
    private static List<HtmlMethonContentDto> htmlMethonContentDtos = new ArrayList<HtmlMethonContentDto>();// 方法的html页面返回list
    private static Map<String, PropertiesParamDto> controllerMethodInfo = new HashMap<>(); //apiDocsData.properties生成Controller中的方法信息

    /**
     * 执行自动生成api方法
     */
    public static void init() {
        LoggerUtil.info("**************************执行自动生成api***************************");
        URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
        String rootPate = classpath.getPath();
        String apiDocsDataUrl = FileUtil.scanFile(rootPate, "apiDocsData", "json");
        //String apiDocsDataUrl = rootPate + "apiDocsData.json";
        String apiDocsUrl = FileUtil.scanFile(rootPate, "apiDocs", "properties");
        String apiDocsDataPropertiesUrl = FileUtil.scanFile(rootPate, "apiDocsData", "properties");
        Properties properties = PropertiesUtil.loadProps(apiDocsUrl);
        String packageNameStr = properties.getProperty("apiDocs.sys.packageName");
        String savePath = properties.getProperty("apiDocs.sys.savePath");
        String isWordStr = properties.getProperty("apiDocs.sys.isWord");
        String isHTMLStr = properties.getProperty("apiDocs.sys.isHTML");
        String charsetCode = properties.getProperty("apiDocs.sys.charsetCode");
        String isShowResponseClassName = properties.getProperty("apiDocs.sys.word.isShowResponseClassName");
        Properties dataProperties = PropertiesUtil.loadProps(apiDocsDataPropertiesUrl, charsetCode, false);

        if (StringUtils.isBlank(packageNameStr)) {
            throw new RuntimeException("在apiDocs.properties配置中没有找到扫瞄包（packageName）的配置");
        }
        if (StringUtils.isBlank(savePath)) {
            throw new RuntimeException("在apiDocs.properties配置中没有找生成api保存的路径（savePath）的配置");
        }

        isWord = isWordStr == "" || isWordStr == null ? true : Boolean.valueOf(isWordStr);
        isHTML = isHTMLStr == "" || isHTMLStr == null ? true : Boolean.valueOf(isHTMLStr);
        if (savePath == null || savePath == "") {
            savePath = rootPate + "apiDocs";
        }
        controllerMethodInfo = PropertiesUtil.getControllerMethodInfo(dataProperties);
        //LoggerUtil.info("controllerMethodInfo:" + new Gson().toJson(controllerMethodInfo));
        String[] packageNames = packageNameStr.split(",");
        for (String packageName : packageNames) {
            generateApi(packageName, savePath);
        }
        if (classExplains.size() > 0) {
            if (isHTML) {
                HtmlTemlateUtil.setApiTemplate(savePath, htmlMethonContentDtos);
                FileUtil.createJsonFile(new Gson().toJson(htmlMethonContentDtos), savePath + "/apiDocs/api-html", "apiData");
            }
            if (isWord) {
                boolean isShow = true;
                if(StringUtil.isNotBlank(isShowResponseClassName)){
                    if(isShowResponseClassName.equals("false")){
                        isShow = false;
                    }
                }
                WordTemlateUtil.setWordTemplate(savePath, wordContentDtos, isShow);
            }
            boolean existFile = FileUtil.isExistFile(apiDocsDataUrl);
            if(existFile){
                String newApiDocsDataUrl = savePath + "/apiDocs/api-html/apiExampleData.json";
                FileUtil.copyFile(apiDocsDataUrl, newApiDocsDataUrl);
            }
        }
        LoggerUtil.info("**************************生成完成***************************");
    }

    /**
     * 生成api
     *
     * @param packageName 包名
     * @param savePath    保存的路径
     *                    （绝对路径，如：‘F:\eclipse-jee-oxyen-workspace\AutomateApiDocs\resources\templates\apiDocs’）
     */
    private static void generateApi(String packageName, String savePath) {
        try {
            List<String> classNames = ClassUtil.getClassName(packageName);
            for (String classNameStr : classNames) {
                ClassExplainDto classExplainDto = new ClassExplainDto(); // 类的头部相关信息
                List<Map<String, String>> methodDescriptions = new ArrayList<>(); // 方法业务说明
                List<MethodInfoDto> methodInfoDtos = new ArrayList<MethodInfoDto>(); // 方法信息

                Class<?> className = Class.forName(classNameStr);
                if (!className.isAnnotationPresent(ApiDocsClass.class)) {
                    LoggerUtil.info(className + "没有在类上注解ApiDocsClass");
                    // 不是生成api文档类
                    continue;
                }

                ClassMoreRemarkDto classMoreRemark = ClassUtil.getClassMoreRemark(className);
                classExplainDto = classMoreRemark.getClassExplainDto(); // 类的头部相关信息
                Map<String, MethodExplainDto> methodExplainDtoMap = classMoreRemark.getMethodExplainDtoMap();//类的方法多行注释Map(key:methodMapKey-0；value:和备注信息)
                if (methodExplainDtoMap == null || methodExplainDtoMap.size() == 0) {
                    continue;
                }

                classExplains.add(classExplainDto);
                StringBuilder path = new StringBuilder();// 请求类路径
                if (className.isAnnotationPresent(RequestMapping.class)) {
                    // 获取类请求路径
                    RequestMapping requestMapping = className.getAnnotation(RequestMapping.class);
                    String[] value = requestMapping.value();
                    for (String str : value) {
                        if (StringUtil.isNotBlank(str)) {
                            if (!str.startsWith("/")) {
                                str = "/" + str;
                            }
                            path.append(str);
                        }
                    }
                }

                for (int i = 0; i < className.getDeclaredMethods().length; i++) {
                    sequence = 0;
                    Method method = className.getDeclaredMethods()[i];
                    String methodName = method.getName();
                    String methodPath = ""; // 方法请求路径
                    if (method.isAnnotationPresent(ApiDocsMethod.class)) {
                        String key = StringUtil.compound(classNameStr, ConstantsUtil.TRANSVERSE_LINE, methodName);
                        PropertiesParamDto propertiesParamDto = controllerMethodInfo.get(key);
                        ApiDocsMethod apiDocs = method.getAnnotation(ApiDocsMethod.class);
                        List<RequestParamDto> requestParamDtos = new ArrayList<>(); // 请求的参数
                        List<ResponseClassDto> responseClassDtos = new ArrayList<ResponseClassDto>(); // 返回数据类信息
                        Class<?> requestBean = apiDocs.requestBean(); // 请求参数Bean
                        Class<?> baseResponseBean = apiDocs.baseResponseBean(); // 响应数据的基础返回Bean
                        Class<?> responseBean = apiDocs.responseBean(); // 响应数据Bean
                        Class<?> baseResponseBeanGenericity = apiDocs.baseResponseBeanGenericity(); //响应数据Bean的泛型真实类型
                        Class<?>[] responseBeans = apiDocs.responseBeans(); // 多个响应数据Bean
                        String methodDescription = apiDocs.methodExplain(); // 方法说明
                        String url = apiDocs.url(); // 请求方法路径
                        String[] value = {}; // 获取方法上的路径
                        String type = apiDocs.type(); // 请求方式
                        String requestBeanCn = ConstantsUtil.DEFAULT_STRING; //请求对象的包名
                        String baseResponseBeanCn = ConstantsUtil.DEFAULT_STRING; //响应基类包名
                        String baseResponseBeanGenericityCn = ConstantsUtil.DEFAULT_STRING; //响应泛型包名
                        String responseBeanCn = ConstantsUtil.DEFAULT_STRING; //响应类包名
                        List<String> responseBeansCns = ConstantsUtil.DEFAULT_COLLECTION; //响应类包名(多个)
                        List<String> requestFalses = ConstantsUtil.DEFAULT_COLLECTION; //请求字段不是必传的（默认必传）
                        List<String> requestIsShowFalse = ConstantsUtil.DEFAULT_COLLECTION; //请求字段不显示（默认显示）
                        List<String> responseIsShowFalse = ConstantsUtil.DEFAULT_COLLECTION; //响应字段不显示（默认显示）
                        String requestBeanJsonKey = apiDocs.requestBeanJsonKey(); //请求参数样例的key
                        String responseBeanJsonKey = apiDocs.responseBeanJsonKey(); //响应结果样例的key
                        if (null != propertiesParamDto) {
                            String  propertiesClassExplain = propertiesParamDto.getClassExplain();
                            if (StringUtil.isNotBlank(propertiesClassExplain) && !ConstantsUtil.DEFAULT_STRING.equals(propertiesClassExplain)) {
                                classExplainDto.setExplain(propertiesClassExplain);
                            }
                            String  propertiesMethodExplain = propertiesParamDto.getMethodExplain();
                            if (StringUtil.isNotBlank(propertiesMethodExplain) && !ConstantsUtil.DEFAULT_STRING.equals(propertiesMethodExplain)) {
                                methodDescription = propertiesMethodExplain;
                            }
                            String propertiesUrl = propertiesParamDto.getUrl();
                            if (StringUtil.isNotBlank(propertiesUrl) && !ConstantsUtil.DEFAULT_STRING.equals(propertiesUrl)) {
                                url = propertiesUrl;
                            }
                            String propertiesType = propertiesParamDto.getType();
                            if (StringUtil.isNotBlank(propertiesType) && !ConstantsUtil.DEFAULT_STRING.equals(propertiesType)) {
                                type = propertiesType;
                            }
                            requestBeanCn = propertiesParamDto.getRequestBeanCn();
                            if (StringUtil.isNotBlank(requestBeanCn) && !ConstantsUtil.DEFAULT_STRING.equals(requestBeanCn)) {
                                Class<?> clzz = Class.forName(requestBeanCn);
                                if (StringUtil.isRealClass(clzz)) {
                                    requestBean = clzz;
                                }
                            }
                            baseResponseBeanCn = propertiesParamDto.getBaseResponseBeanCn();
                            if (StringUtil.isNotBlank(baseResponseBeanGenericityCn) && !ConstantsUtil.DEFAULT_STRING.equals(baseResponseBeanCn)) {
                                Class<?> clzz = Class.forName(baseResponseBeanCn);
                                if (StringUtil.isRealClass(clzz)) {
                                    baseResponseBean = clzz;
                                }
                            }
                            baseResponseBeanGenericityCn = propertiesParamDto.getBaseResponseBeanGenericityCn();
                            if (StringUtil.isNotBlank(baseResponseBeanGenericityCn) && !ConstantsUtil.DEFAULT_STRING.equals(baseResponseBeanGenericityCn)) {
                                Class<?> clzz = Class.forName(baseResponseBeanGenericityCn);
                                if (StringUtil.isRealClass(clzz)) {
                                    baseResponseBeanGenericity = clzz;
                                }
                            }
                            responseBeanCn = propertiesParamDto.getResponseBeanCn();
                            if (StringUtil.isNotBlank(responseBeanCn) && !ConstantsUtil.DEFAULT_STRING.equals(responseBeanCn)) {
                                Class<?> clzz = Class.forName(responseBeanCn);
                                if (StringUtil.isRealClass(clzz)) {
                                    responseBean = clzz;
                                }
                            }
                            responseBeansCns = propertiesParamDto.getResponseBeansCns();
                            if (null != responseBeansCns && responseBeansCns.size() > 0) {
                                int index = 0;
                                for (String responseBeansCn : responseBeansCns) {
                                    if(StringUtil.isNotBlank(responseBeansCn)){
                                        Class<?> clzz = Class.forName(responseBeansCn);
                                        if (StringUtil.isRealClass(clzz)) {
                                            responseBeans[index] = clzz;
                                            index++;
                                        }
                                    }
                                }
                            }
                            requestFalses = propertiesParamDto.getRequestFalses();
                            requestIsShowFalse = propertiesParamDto.getRequestIsShowFalse();
                            responseIsShowFalse = propertiesParamDto.getResponseIsShowFalse();
                            String pRequestBeanJsonKey = propertiesParamDto.getRequestBeanJsonKey();
                            if (StringUtil.isNotBlank(pRequestBeanJsonKey) && !ConstantsUtil.DEFAULT_STRING.equals(pRequestBeanJsonKey)) {
                                requestBeanJsonKey = pRequestBeanJsonKey;
                            }
                            String pResponseBeanJsonKey = propertiesParamDto.getResponseBeanJsonKey();
                            if (StringUtil.isNotBlank(pResponseBeanJsonKey) && !ConstantsUtil.DEFAULT_STRING.equals(pResponseBeanJsonKey)) {
                                responseBeanJsonKey = pResponseBeanJsonKey;
                            }

                        }
                        //获取方法请求类型
                        if((StringUtil.isBlank(type) || StringUtils.isBlank(url))){
                            if (method.isAnnotationPresent(RequestMapping.class)) {
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                                value = requestMapping.value();
                                //获取requestMapping中的请求方式
                                RequestMethod[] requestMethods = requestMapping.method();
                                for (RequestMethod requestMethod : requestMethods) {
                                    if (requestMethod.equals(RequestMethod.GET)) {
                                        type = "GET";
                                    } else if (requestMethod.equals(RequestMethod.POST)) {
                                        type = "POST";
                                    } else {
                                        continue;
                                    }
                                }
                            } else if (method.isAnnotationPresent(PostMapping.class)) {
                                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                                value = postMapping.value();
                                type = "POST";
                            } else {
                                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                                value = getMapping.value();
                                type = "GET";
                            }
                        }
                        //获取方法的请求路径
                        if (StringUtils.isBlank(url)) {
                            for (String str : value) {
                                if (StringUtil.isNotBlank(str)) {
                                    if (!str.startsWith("/")) {
                                        str = "/" + str;
                                    }
                                    String sPath = path.toString();
                                    int lastIndex = sPath.lastIndexOf("/");
                                    if(lastIndex == (sPath.length() - 1)){
                                        sPath = sPath.substring(0, lastIndex);
                                    }
                                    methodPath = sPath + str;
                                }
                            }

                            url = StringUtils.isBlank(url) ? methodPath : path + url;
                        }

                        MethodExplainDto methodExplainDto = methodExplainDtoMap.get(url);
                        if(null == methodExplainDto){
                            throw new RuntimeException("当前类：" + classNameStr+"."+methodName+"() 方法的路径为：" + url + " 不正确，请核实！");
                        }
                        requestParamDtos = methodExplainDto.getParamDtos();
                        if (StringUtils.isBlank(methodDescription)) {
                            methodDescription = methodExplainDto.getExplain();
                        }
                        Map<String, String> methodDescriptionMap = new LinkedHashMap<>();
                        String methodKey = UUID.randomUUID().toString();
                        methodDescriptionMap.put(ConstantsUtil.METHOD_DESCRIPTION_VALUE_KEY, methodDescription);
                        methodDescriptionMap.put(ConstantsUtil.METHOD_KEY, methodKey);

                        List<ResponseDataDto> baseResponseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息(基础类)
                        List<ResponseDataDto> responseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息
                        //获取请求参数信息
                        if (StringUtil.isRealClass(requestBean)) {
                            List<ClassFiedInfoDto> requestFieldInfos = ClassUtil.getClassFieldAndMethod(requestBean,
                                    true, 1);
                            if (requestFieldInfos != null && requestFieldInfos.size() > 0) {
                                requestParamDtos.clear(); // 清空多行注释中的参数，以ApiDocs中的为主
                                for (ClassFiedInfoDto classFiedInfoDto : requestFieldInfos) {
                                    String name = classFiedInfoDto.getName();
                                    String parentNode = classFiedInfoDto.getParentNode();
                                    Boolean ifPass = classFiedInfoDto.getIfPass();
                                    //字段是否要显示
                                    boolean isShow = true;
                                    if (null != requestIsShowFalse && requestIsShowFalse.size() > 0) {
                                        for (String requestIsShowFals : requestIsShowFalse) {
                                            if (StringUtil.isNotBlank(requestIsShowFals) && requestIsShowFals.contains(requestBean.getName())
                                            && (requestIsShowFals.contains(name) || (StringUtil.isNotBlank(parentNode) && requestIsShowFals.contains(parentNode)))) {
                                                isShow = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (isShow) {
                                        RequestParamDto requestParamDto = new RequestParamDto();
                                        requestParamDto.setName(name);
                                        requestParamDto.setType(classFiedInfoDto.getType());
                                        requestParamDto.setDescription(classFiedInfoDto.getDescription());
                                        ifPass = ifPass == null ? true : ifPass;
                                        //设置请求参数是否为必传
                                        if (null != requestFalses && requestFalses.size() > 0) {
                                            for (String requestFalse : requestFalses) {
                                                if (StringUtil.isNotBlank(requestFalse) && requestFalse.contains(name)) {
                                                    ifPass = false;
                                                    break;
                                                }
                                            }
                                        }
                                        requestParamDto.setRequired(ifPass);
                                        requestParamDtos.add(requestParamDto);
                                    }
                                }
                            }
                        }
                        if (StringUtil.isRealClass(responseBean)) {
                            List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBean,
                                    true, 1);
                            saveFiledInfo(responseBean, responseFieldInfos, responseDataDtos, responseIsShowFalse);
                        }

                        if (ClassUtil.isRealClass(baseResponseBeanGenericity)) {
                            List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(baseResponseBeanGenericity,
                                    true, 1);
                            saveFiledInfo(baseResponseBeanGenericity, responseFieldInfos, responseDataDtos, responseIsShowFalse);
                        }


                        if (responseBeans != null && responseBeans.length > 0) {
                            for (int j = 0; j < responseBeans.length; j++) {
                                Class<?> responseBeanClass = responseBeans[j];
                                if (StringUtil.isRealClass(responseBeanClass)) {
                                    List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
                                            .getClassFieldAndMethod(responseBeanClass, true, 1);
                                    saveFiledInfo(responseBeanClass, responseFieldInfos, responseDataDtos, responseIsShowFalse);
                                }
                            }
                        }

                        if (StringUtil.isRealClass(baseResponseBean)) {
                            // 有基础类返回
                            List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
                                    .getClassFieldAndMethod(baseResponseBean, true, 1);
                            if (responseFieldInfos != null && responseFieldInfos.size() > 0) {
                                for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
                                    String name = classFiedInfoDto.getName();
                                    String fiedInfoType = classFiedInfoDto.getType();
                                    String parentNode = classFiedInfoDto.getParentNode();
                                    //字段是否要显示
                                    boolean isShow = true;
                                    if (null != responseIsShowFalse && responseIsShowFalse.size() > 0) {
                                        for (String responseIsShowFals : responseIsShowFalse) {
                                            if (StringUtil.isNotBlank(responseIsShowFals) && responseIsShowFals.contains(baseResponseBean.getName()) && responseIsShowFals.contains(name)) {
                                                isShow = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (isShow && StringUtil.isBlank(parentNode)) {
                                        ResponseDataDto responseDataDto = new ResponseDataDto();
                                        responseDataDto.setName(name);
                                        responseDataDto.setType(classFiedInfoDto.getType());
                                        responseDataDto.setDescription(classFiedInfoDto.getDescription());
                                        String[] types = {"list", "set", "object", "map"};
                                        List<String> typesList = Arrays.asList(types);
                                        if (typesList.contains(fiedInfoType)) {
                                            // 存在
                                            responseDataDto.setResponseDataDtos(responseDataDtos);
                                        }else if(fiedInfoType.equals("class")){
                                            List<ResponseDataDto> childResponseDataDtos = getBaseResponseChildClassData(responseFieldInfos, name);
                                            responseDataDto.setResponseDataDtos(childResponseDataDtos);
                                        }
                                        baseResponseDataDtos.add(responseDataDto);
                                    }
                                }
                            }
                        }

                        ArrayList<String> groupKeyList = new ArrayList<String>(); // 来确定分组key集合
                        List<ResponseDataDto> list = null;
                        List<ResponseClassDto> responseClassDtoList = new ArrayList<ResponseClassDto>();
                        int responseClassDtoIndex = -1;
                        for (ResponseDataDto responseDataDto : responseDataDtos) {
                            String csName = responseDataDto.getClassName();
                            String name = responseDataDto.getName();
                            String fieldType = responseDataDto.getType();
                            String parentNode = responseDataDto.getParentNode();
                            //字段是否要显示
                            boolean isShow = true;
                            if (null != responseIsShowFalse && responseIsShowFalse.size() > 0) {
                                for (String responseIsShowFals : responseIsShowFalse) {
                                    if (StringUtil.isNotBlank(responseIsShowFals) && responseIsShowFals.contains(csName)
                                            && (responseIsShowFals.contains(name) || (StringUtil.isNotBlank(parentNode) && responseIsShowFals.contains(parentNode)))) {
                                        isShow = false;
                                        break;
                                    }
                                }
                            }
                            if (isShow) {
                                int grade = responseDataDto.getGrade();
                                String groupKey = csName; // 分组的key
                                boolean containsKey = groupKeyList.contains(groupKey);
                                if (containsKey) {
                                    list.add(responseDataDto);
                                } else {
                                    responseClassDtoIndex++;
                                    list = new ArrayList<ResponseDataDto>();
                                    list.clear();
                                    list.add(responseDataDto);
                                    groupKeyList.add(groupKey);
                                }
                                String[] fieldTypeArr = {"list", "class"};
                                if (StringUtil.containsStr(fieldTypeArr, fieldType)) {
                                    // 如果字段是以上数组，级别就减1
                                    grade--;
                                }
                                // classNameMap.put(groupKey, list);
                                ResponseClassDto responseClassDto = new ResponseClassDto();
                                responseClassDto.setGrade(grade);
                                responseClassDto.setClassName(csName);
                                responseClassDto.setFieldType(fieldType);
                                responseClassDto.setResponseDataDtos(list);
                                if (responseClassDtoList.size() - 1 == responseClassDtoIndex) {
                                    responseClassDtoList.remove(responseClassDtoIndex);
                                }
                                responseClassDtoList.add(responseClassDtoIndex, responseClassDto);
                            }
                        }
                        for (ResponseClassDto responseClassDto : responseClassDtoList) {
                            responseClassDtos.add(responseClassDto);
                        }

                        methodDescriptions.add(methodDescriptionMap);
                        MethodInfoDto methodInfoDto = new MethodInfoDto();
                        methodInfoDto.setMethodDescription(methodDescription);
                        methodInfoDto.setType(type);
                        methodInfoDto.setUrl(url);
                        methodInfoDto.setRequestParamDtos(requestParamDtos);
                        methodInfoDto.setResponseClassDtos(responseClassDtos);
                        methodInfoDto.setBaseResponseDataDtos(baseResponseDataDtos);
                        methodInfoDto.setMethodKey(methodKey);
                        methodInfoDto.setRequestBeanJsonKey(requestBeanJsonKey);
                        methodInfoDto.setResponseBeanJsonKey(responseBeanJsonKey);
                        methodInfoDtos.add(methodInfoDto);
                        LoggerUtil.info("####################################################################");
                        LoggerUtil.info("类的说明 ：" + classExplainDto.getExplain());
                        LoggerUtil.info("方法业务说明：" + methodDescription);
                        LoggerUtil.info("方法请求路径：" + url);
                        LoggerUtil.info("请求方法方式：" + type);
                        LoggerUtil.info("请求字段信息：" + new Gson().toJson(requestParamDtos));
                        LoggerUtil.info("响应字段信息：" + new Gson().toJson(responseClassDtos));
                        LoggerUtil.info("响应字段basRespons信息：" + new Gson().toJson(baseResponseDataDtos));
                        LoggerUtil.info("methodKey：" + methodKey);
                        LoggerUtil.info("####################################################################");
                    }
                }
                if (isHTML) {

                    HtmlMethonContentDto htmlMethonContentDto = new HtmlMethonContentDto(classExplainDto,
                            methodDescriptions, methodInfoDtos);
                    htmlMethonContentDtos.add(htmlMethonContentDto);
                }
                if (isWord) {
                    WordContentDto wordContentDto = new WordContentDto();
                    wordContentDto.setMethodInfoDtos(methodInfoDtos);
                    wordContentDto.setClassExplainDto(classExplainDto);
                    wordContentDtos.add(wordContentDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取字段中的子类的信息
     * @author wangmingchang
     * @date 2019/2/1 11:20
     * @param responseFieldInfos
     * @param parentName
     * @return
     **/
    private static List<ResponseDataDto> getBaseResponseChildClassData(List<ClassFiedInfoDto> responseFieldInfos, String parentName) {
        List<ResponseDataDto> responseDataDtos = new ArrayList<>();
        for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos){
            String parentNode = classFiedInfoDto.getParentNode();
            String name = classFiedInfoDto.getName();
            String type = classFiedInfoDto.getType();
            if(StringUtil.isNotBlank(parentNode) && parentName.equals(parentNode)){
                ResponseDataDto responseDataDto = new ResponseDataDto();
                responseDataDto.setName(name);
                responseDataDto.setType(classFiedInfoDto.getType());
                responseDataDto.setDescription(classFiedInfoDto.getDescription());
                String[] types = {"list", "set", "object", "map","class"};
                List<String> typesList = Arrays.asList(types);
                if (typesList.contains(type)) {
                    // 存在
                    List<ResponseDataDto> childResponseDataDtos = getBaseResponseChildClassData(responseFieldInfos, name);
                    responseDataDto.setResponseDataDtos(childResponseDataDtos);
                }
                responseDataDtos.add(responseDataDto);
            }
        }
        return responseDataDtos;
    }

    /**
     * 保存类的字段相关信息
     *
     * @param obj                类
     * @param responseFieldInfos 类的字段信息
     * @param responseDataDtos   返回的总字段信息的集合
     * @param responseIsShowFalse 不显示字段
     * @throws Exception
     * @throws Exception
     */
    private static void saveFiledInfo(Class<?> obj, List<ClassFiedInfoDto> responseFieldInfos,
                                      List<ResponseDataDto> responseDataDtos, List<String> responseIsShowFalse) throws Exception, Exception {
        List<ClassFiedInfoDto> classFiedInfoDtoList = new CopyOnWriteArrayList<ClassFiedInfoDto>();
        classFiedInfoDtoList = responseFieldInfos;
        if (classFiedInfoDtoList != null && classFiedInfoDtoList.size() > 0) {
            Map<String, List<ResponseDataDto>> map = new HashMap<String, List<ResponseDataDto>>();
            List<String> existList = new ArrayList<>();
            for (ClassFiedInfoDto classFiedInfoDto : classFiedInfoDtoList) {
                String parentNode = classFiedInfoDto.getParentNode();
                String name = classFiedInfoDto.getName();
                String type = classFiedInfoDto.getType();
                //字段是否要显示
                boolean isShow = true;
                if (null != responseIsShowFalse && responseIsShowFalse.size() > 0) {
                    for (String responseIsShowFals : responseIsShowFalse) {
                        if (StringUtil.isNotBlank(responseIsShowFals)) {
                            String newResponseIsShowFals = StringUtil.substringAfterLast(responseIsShowFals, ".");
                            if(responseIsShowFals.contains(obj.getName()) && newResponseIsShowFals.equals(name)){
                                isShow = false;
                                if(type.equals(ConstantsUtil.FIELD_SCOPE_CLASS)){
                                    existList.add(name);
                                }
                                break;
                            }else if(StringUtil.isNotBlank(parentNode) && existList.contains(parentNode)){
                                isShow = false;
                                break;
                            }

                        }
                    }
                }
                if(isShow){
                    ResponseDataDto responseDataDto = new ResponseDataDto();
                    responseDataDto.setGrade(classFiedInfoDto.getGrade());
                    responseDataDto.setClassName(obj.getSimpleName());
                    responseDataDto.setName(name);

                    sequence++;
                    responseDataDto.setSequence(sequence);
                    responseDataDto.setType(classFiedInfoDto.getType());
                    responseDataDto.setDescription(classFiedInfoDto.getDescription());
                    String childNode = classFiedInfoDto.getChildNode();
                    if (childNode != null) {
                        responseDataDto.setChildNode(childNode);
                        boolean containsKey = map.containsKey(childNode);
                        if (containsKey) {
                            // 有子节点
                            responseDataDto.setResponseDataDtos(map.get(childNode));
                        }
                    }

                    if (parentNode != null) {
                        boolean containsKey = map.containsKey(parentNode);
                        List<ResponseDataDto> list = new ArrayList<ResponseDataDto>();
                        if (containsKey) {
                            list = map.get(parentNode);
                            list.add(responseDataDto);
                        } else {
                            list.add(responseDataDto);
                        }
                        // 将父节点名的为key,子节点的对象为value
                        map.put(parentNode, list);
                        continue;
                    }
                    responseDataDtos.add(responseDataDto);
                }

            }

        }
    }

}
