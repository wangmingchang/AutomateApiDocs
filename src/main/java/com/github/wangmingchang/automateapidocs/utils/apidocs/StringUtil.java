package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangmingchang
 * @data 2019\1\20 0020 22:13
 **/
public class StringUtil extends StringUtils {

    /**
     * 去除空格(包括换行)
     *
     * @param str
     *            字符串
     * @return 没有空格的字符串
     */
    public static String replaceBlankAll(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 去除换行
     *
     * @param str
     *            字符串
     * @return 没有换行的字符串
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            // Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Pattern p = Pattern.compile("\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    /**
     * 去除自定义字符串
     *
     * @param source 字符串
     * @param regex 替换字符
     * @param replacement 替换后字符
     *
     * @return 没有换行的字符串
     */
    public static String replaceCustomBlank(String source, String regex, String replacement) {
        String dest = "";
        if (source != null) {
            if(source.indexOf(regex) != -1){
                String[] arr = source.split(regex);
                for (String s : arr){
                    dest += replaceBlankAll(s);
                }
            }
        }
        return dest;
    }
    /**
     * 去除自定义字符串
     *
     * @param source 字符串
     * @param regexs 替换字符数组
     * @param replacement 替换后字符
     *
     * @return 没有换行的字符串
     */
    public static String replaceCustomBlank(String source, String[] regexs, String replacement) {
        String dest = "";
        if (source != null) {
            for (String regex : regexs){
                if(source.indexOf(regex) != -1){
                    String[] arr = source.split(regex);
                    for (String s : arr){
                        dest += replaceBlankAll(s);
                    }
                }
            }
        }
        return dest;
    }

    /**
     * 去除星标*
     *
     * @param str
     *            字符串
     * @return 没有换行的字符串
     */
    public static String replaceStarBlank(String str) {
        String dest = "";
        if (str != null) {
            // Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Pattern p = Pattern.compile("\\*");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 判断String数组是否存在某个值
     *
     * @param arr
     *            数组
     * @param targetValue
     *            值
     * @return 判断String数组是否存在某个值
     */
    public static boolean containsStr(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue))
                return true;
        }
        return false;
    }

    /**
     * 判断class是否可真实的对象
     *
     * @param cls
     *            class
     * @return 判断class是否可真实的对象
     */
    public static boolean isRealClass(Class<?> cls) {
        boolean flag = false;
        if (cls != ApiDocsClass.Null.class && cls != ApiDocsMethod.class && !cls.toString().contains("$Null")) {
            flag = true;
        }
        return flag;
    }
    /**
     * 判断字符串是否包含某个数组中的字符串
     * @author wmc
     * @date 2019\1\20
     * @Param [str,arr]
     * @return boolean
     **/
    public static boolean indexOf(String str ,String[] arr){
        boolean flag = false;
        for (String s : arr){
            if(str.indexOf(s) != -1 ){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
