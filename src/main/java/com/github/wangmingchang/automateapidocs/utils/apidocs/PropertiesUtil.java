package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.pojo.apidocs.PropertiesParamDto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
    private static Map<String, Map<String, Object>> methodInfoMap = new HashMap<String, Map<String, Object>>();
    /**
     * 保存监听controller的根路径的信息map,key:${reference.request}
     */
    private static Map<String, String> referenceMap = new HashMap<String, String>();
    /**
     * 保存Controller的配置的信息map,key:${controller.HtmlController}
     */
    private static Map<String, String> controllerMap = new HashMap<String, String>();
    /**
     * 保存C请求和响应的实体的信息map,key:${entry.Fish}
     */
    private static Map<String, String> entryMap = new HashMap<String, String>();

    /**
     * 加载属性文件
     *
     * @param filePath 文件路径
     * @return 加载属性文件
     */
    public static Properties loadProps(String filePath) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8");
            properties.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
    /**
     * 加载属性文件
     *
     * @param filePath 文件路径
     * @param isThrow 是否抛错
     * @return 加载属性文件
     */
    public static Properties loadProps(String filePath, boolean isThrow){
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(filePath);
                InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8");
                properties.load(reader);
            } catch (Exception e) {
                if(isThrow){
                    e.printStackTrace();
                }
            }finally {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            }
        }catch (Exception e){

        }
        return properties;
    }

    /**
     * 加载属性文件
     *
     * @param filePath     文件路径
     * @param charsentCode 编码格式
     * @return 加载属性文件
     */
    public static Properties loadProps(String filePath, String charsentCode) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            if (StringUtil.isBlank(charsentCode)) {
                charsentCode = ConstantsUtil.DEFAULT_CHARSET_CODE;
            }
            InputStreamReader reader = new InputStreamReader(fileInputStream, charsentCode);
            properties.load(reader);
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
     * 根据配置文件获取Controller的方法信息
     *
     * @param properties
     * @return key如：com.github.wangmingchang.automateapidocs.controller-index，value:方法的信息
     * @author wangmingchang
     * @date 2019/1/28 10:00
     **/
    public static Map<String, PropertiesParamDto> getControllerMethodInfo(Properties properties) {
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
                mapKey = "${reference." + mapKey + "}";
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
        controllerMap = replaceValue(controllerMap, referenceMap);
        entryMap = replaceValue(entryMap, referenceMap);
        Set<Map.Entry<String, String>> controllerEntries = controllerMap.entrySet();
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
                setMethodInfo(methodKey, methodValue);
            }
            Set<Map.Entry<String, Map<String, Object>>> methodInfoEntries = methodInfoMap.entrySet();

            for (Map.Entry<String, Map<String, Object>> methodInfoEntry : methodInfoEntries) {
                PropertiesParamDto propertiesParamDto = new PropertiesParamDto();
                propertiesParamDto.setClassName(controllerValue);
                String methodInfoKey = methodInfoEntry.getKey();
                Map<String, Object> methodInfoValueMap = methodInfoEntry.getValue();
                Set<Map.Entry<String, Object>> valueEntries = methodInfoValueMap.entrySet();
                for (Map.Entry<String, Object> entry : valueEntries) {
                    String methodKey = entry.getKey();
                    Object infoValue = entry.getValue();
                    if (methodKey.equals(ConstantsUtil.PROPERTIES_CONTROLLER_METHOD_NAME)) {
                        propertiesParamDto.setMethodName(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_URL)) {
                        propertiesParamDto.setUrl(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_TYP)) {
                        propertiesParamDto.setType(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_METHOD_EXPLAIN)) {
                        propertiesParamDto.setMethodExplain(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_REQUEST_BEAN)) {
                        propertiesParamDto.setRequestBeanCn(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN)) {
                        propertiesParamDto.setBaseResponseBeanCn(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY)) {
                        propertiesParamDto.setBaseResponseBeanGenericityCn(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEAN)) {
                        propertiesParamDto.setResponseBeanCn(String.valueOf(infoValue));
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS)) {
                        propertiesParamDto.setResponseBeansCns((List<String>) infoValue);
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE)) {
                        propertiesParamDto.setRequestFalses((List<String>) infoValue);
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE)) {
                        propertiesParamDto.setRequestIsShowFalse((List<String>) infoValue);
                    } else if (methodKey.equals(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE)) {
                        propertiesParamDto.setResponseIsShowFalse((List<String>) infoValue);
                    }
                }
                propertiesParamDtoMap.put(methodInfoKey, propertiesParamDto);
            }
        }


        return propertiesParamDtoMap;
    }

    /**
     * 设置每个方法信息
     *
     * @param methodKey
     * @param methodValue
     * @return
     * @author wangmingchang
     * @date 2019/1/29 14:19
     **/
    private static void setMethodInfo(String methodKey, String methodValue) {
        Map<String, Object> map = new HashMap<>();
        String[] keyArr = methodKey.split("\\.");
        String methodName = "";
        for (String key : keyArr) {
            if (key.contains("()")) {
                methodName = key.split("\\(\\)")[0];
                break;
            }
        }
        int index = methodKey.indexOf("}") + 1;
        //获取方法所在的controller的包名
        String controllKey = methodKey.substring(0, index);
        String controllValue = controllerMap.get(controllKey);
        String key = StringUtil.compound(controllValue , ConstantsUtil.TRANSVERSE_LINE , methodName);

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
        String newMethodKey = StringUtil.substringAfter(methodKey,"()");
        newMethodKey = newMethodKey.substring(1, newMethodKey.length());
        if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_URL)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_URL, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_TYP)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_TYP, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_METHOD_EXPLAIN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_METHOD_EXPLAIN, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_REQUEST_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_REQUEST_BEAN, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_BASE_RESPONSE_BEAN_GENERICITY, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEAN)) {
            map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEAN, infoValue);
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS, infoList);
            } else {
                infoList.add(infoValue);
                map.put(ConstantsUtil.APIDOCS_METHOD_RESPONSE_BEANS, infoList);
            }
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE, infoList);
            } else {
                infoList.add(infoValue);
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_FALSE, infoList);
            }
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE, infoList);
            } else {
                infoList.add(infoValue);
                map.put(ConstantsUtil.APIDOCS_PARAM_REQUEST_ISSHOW_FALSE, infoList);
            }
        } else if (newMethodKey.equals(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE)) {
            if (infoList.size() > 0) {
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE, infoList);
            } else {
                infoList.add(infoValue);
                map.put(ConstantsUtil.APIDOCS_PARAM_RESPONSE_ISSHOW_FALSE, infoList);
            }
        }
        if (!map.isEmpty()) {
            String existMethodName = (String) map.get(ConstantsUtil.PROPERTIES_CONTROLLER_METHOD_NAME);
            if (StringUtil.isBlank(existMethodName)) {
                map.put(ConstantsUtil.PROPERTIES_CONTROLLER_METHOD_NAME, methodName);
            }
            methodInfoMap.put(key, map);
        }

    }

    /**
     * 解析方法信息的value
     *
     * @param source
     * @param entryMap
     * @return
     * @author wangmingchang
     * @date 2019/1/29 15:18
     **/
    private static String getValueInfo(String source, Map<String, String> entryMap) {
        source = StringUtil.removeSymbol(source);
        Set<Map.Entry<String, String>> entries = entryMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (source.contains(key)) {
                source = StringUtil.replaceCustomBlank(source, key, value);
                break;
            }
        }
        return source;
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
