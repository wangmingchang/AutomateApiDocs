package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.pojo.apidocs.PropertiesParamDto;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * properties配置文件工具类
 *
 * @author 王明昌
 * @since 2017年9月16日
 */
public class PropertiesUtil {

    /**
     * 保存方法的信息map,key:方法名称
     */
    private static Map<String, Map<String, Object>> methodInfoMap = new HashMap();
    /**
     * 保存监听controller的根路径的信息map,key:${reference.request}
     */
    private static Map<String, String> referenceMap = new HashMap<>();
    /**
     * 保存Controller的配置的信息map,key:${controller.HtmlController}
     */
    private static Map<String, String> controllerMap = new HashMap<>();
    /**
     * 保存C请求和响应的实体的信息map,key:${entry.Fish}
     */
    private static Map<String, String> entryMap = new HashMap<>();

    /**
     * 加载属性文件
     *
     * @param filePath 文件路径
     * @return 加载属性文件
     */
    public static Properties loadProps(String filePath) {
        Properties properties = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 读取配置文件
     *
     * @param properties properties文件
     * @param key        key值
     * @return 取配置文件
     */
    public static String getString(Properties properties, String key) {
        return properties.getProperty(key);
    }

    /**
     * * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插入一对键值。
     *
     * @param properties properties文件
     * @param filePath   路径
     * @param keyname    key值
     * @param keyvalue   value值
     */
    public static void updateProperty(Properties properties, String filePath, String keyname, String keyvalue) {
        try {

            // 从输入流中读取属性列表（键和元素对）
            properties.setProperty(keyname, keyvalue);
            FileOutputStream outputFile = new FileOutputStream(filePath);
            properties.store(outputFile, null);
            outputFile.flush();
            outputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据配置文件获取Controller
     *
     * @param properties
     * @return
     * @author wangmingchang
     * @date 2019/1/28 10:00
     **/
    public static Object getController(Properties properties) {
        //key为controller的包名+方法名称
        Map<String, PropertiesParamDto> propertiesParamDtoMap = new HashMap<>();

        Map<String, Map<String, String>> controllerMethodMap = new HashMap<>();
        Set<String> propertiesKeys = properties.stringPropertyNames();
        for (String key : propertiesKeys) {
            if (key.contains(ConstantsUtil.PROPERTIES_SYS)) {
                continue;
            }
            String propertyVaule = properties.getProperty(key);
            String mapKey = key;
            if (key.contains(ConstantsUtil.PROPERTIES_REFERENCE)) {
                mapKey = key.split(ConstantsUtil.PROPERTIES_REFERENCE)[1];
                mapKey = "${refernce." + mapKey + "}";
                referenceMap.put(mapKey, propertyVaule);
            } else if (key.contains(ConstantsUtil.PROPERTIES_CONTROLLER)) {
                mapKey = key.split(ConstantsUtil.PROPERTIES_CONTROLLER)[1];
                mapKey = "${controller." + mapKey + "}";
                controllerMap.put(mapKey, propertyVaule); //${controller.HtmlController}
            } else if (key.contains(ConstantsUtil.PROPERTIES_ENTRY)) {
                mapKey = key.split(ConstantsUtil.PROPERTIES_ENTRY)[1];
                mapKey = "entry." + mapKey;
                entryMap.put(mapKey, propertyVaule);
            } else if (key.startsWith(ConstantsUtil.PROPERTIES_CONTROLLER_START)) {
                String controllerKey = key.split("}")[0] + "}"; //${controller.HtmlController}
                Map<String, String> methodMap = controllerMethodMap.get(controllerKey);
                if (null != methodMap && methodMap.size() > 0) {
                    methodMap.put(key, propertyVaule);
                } else {
                    methodMap = new HashMap<>();
                    methodMap.put(key, propertyVaule);
                }
                controllerMethodMap.put(controllerKey, methodMap);
            } else {
                continue;
            }
        }
        Set<Map.Entry<String, String>> referenceEntries = referenceMap.entrySet();
        Set<Map.Entry<String, String>> entryEntries = entryMap.entrySet();
        controllerMap = replaceValue(controllerMap, referenceMap);
        entryMap = replaceValue(entryMap, referenceMap);
        Set<Map.Entry<String, String>> controllerEntries = controllerMap.entrySet();
        PropertiesParamDto propertiesParamDto = new PropertiesParamDto();
        for (Map.Entry<String, String> controllerEntry : controllerEntries) {
            String controllerKey = controllerEntry.getKey();
            //controller的包名
            String controllerValue = controllerEntry.getValue();
            Map<String, String> methodMap = controllerMethodMap.get(controllerKey);
            //方法信息
            Set<Map.Entry<String, String>> methodEntries = methodMap.entrySet();
            for (Map.Entry<String, String> methodEntry : methodEntries) {
                String methodKey = methodEntry.getKey();
                String methodValue = methodEntry.getValue();
                getMethodInfo(methodKey, methodValue);
            }
            Set<Map.Entry<String, Map<String, Object>>> methodInfoEntries = methodInfoMap.entrySet();

            for (Map.Entry<String, Map<String, Object>> methodInfoEntry : methodInfoEntries){
                String key = methodInfoEntry.getKey();
                Map<String, Object> value = methodInfoEntry.getValue();



            }


        }
        Set<Map.Entry<String, Map<String, String>>> controllMotheEntries = controllerMethodMap.entrySet();
        for (Map.Entry<String, Map<String, String>> controllerMethodEntry : controllMotheEntries) {

        }

        return null;
    }

    /**
     * 获取每个方法信息
     *
     * @param methodKey
     * @param methodValue
     * @return
     * @author wangmingchang
     * @date 2019/1/29 14:19
     **/
    private static void getMethodInfo(String methodKey, String methodValue) {
        Map<String, Object> map = new HashMap<>();
        String[] keyArr = methodKey.split(".");
        String methodName = "";
        for (String key : keyArr) {
            if (key.contains("()")) {
                methodName = key.split("()")[0];
                break;
            }
        }
        int index = methodKey.indexOf(".", 2);
        //获取方法所在的controller的包名
        String controllKey = methodKey.substring(0, index);
        String controllValue = controllerMap.get(controllKey);
        String key = controllValue + "-" + methodName;
        Map<String, Object> existMap = methodInfoMap.get(key);
        if (null != existMap && !existMap.isEmpty()) {
            map = existMap;
        }

        String infoValue = "";
        List<String> infoList = new ArrayList<>();
        if (methodValue.contains("${")) {
            String[] valueArr = methodValue.split(",");
            if (valueArr.length > 1) {
                for (String value : valueArr) {
                    infoList.add(getValueInfo(value, entryMap));
                }
            } else {
                infoValue = getValueInfo(methodValue, entryMap);
            }

        } else {
            infoValue = methodValue;
        }

        if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_URL)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_URL, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_TYP)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_TYP, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_METHOD_EXPLAIN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_METHOD_EXPLAIN, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_REQUEST_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_REQUEST_BEAN, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEAN, infoValue);
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_REQUEST_TRUE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_TRUE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_TRUE, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_TRUE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_TRUE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_TRUE, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_TRUE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_TRUE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_TRUE, infoValue);
            }
        } else if (methodKey.contains(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE, infoList);
            } else {
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE, infoValue);
            }

        }
        if (!map.isEmpty()) {
            map.put(ConstantsUtil.PROPERTIES_CONTROLLER_METHOD_NAME, methodName);
            PropertiesUtil.methodInfoMap.put(key, map);
        }

    }

    /**
     * 解析方法信息的value
     *
     * @param value
     * @param entryMap
     * @return
     * @author wangmingchang
     * @date 2019/1/29 15:18
     **/
    private static String getValueInfo(String value, Map<String, String> entryMap) {
        value = StringUtil.removeSymbol(value);
        int index = value.indexOf(".", 2);
        String key = value.substring(0, index);
        String entryValue = entryMap.get(key);
        value = StringUtil.replaceCustomBlank(value, key, entryValue);
        return value;
    }


    /**
     * 替换${}对象的值
     *
     * @param source
     * @param target
     * @return
     * @author wangmingchang
     * @date 2019/1/29 11:11
     **/
    private static Map<String, String> replaceValue(Map<String, String> source, Map<String, String> target) {
        Set<Map.Entry<String, String>> sourceEntries = source.entrySet();
        Set<Map.Entry<String, String>> targetEntries = target.entrySet();
        for (Map.Entry<String, String> entry : sourceEntries) {
            String value = entry.getValue();
            for (Map.Entry<String, String> targetEntry : targetEntries) {
                String targetKey = targetEntry.getKey();
                String targetValue = targetEntry.getValue();
                if (value.contains(targetKey)) {
                    value = StringUtil.replaceCustomBlank(value, targetKey, targetValue);
                    entry.setValue(value);
                    break;
                }
            }
        }
        return source;
    }

}
