package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangmingchang
 * @data 2019\1\20 0020 22:13
 **/
public class StringUtil extends StringUtils {

    private static final String START_INDEX_KEY = "startIndex";
    private static final String END_INDEX_KEY = "endIndex";

    /**
     * 去除空格(包括换行)
     *
     * @param str 字符串
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
     * @param str 字符串
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
     * @param source      字符串
     * @param regex       替换字符
     * @param replacement 替换后字符
     * @return 没有换行的字符串
     */
    public static String replaceCustomBlank(String source, String regex, String replacement) {
        String dest = "";
        if (source != null) {
            if (source.indexOf(regex) != -1) {
                if (isNotBlank(replacement)) {
                    List<Map<String, Integer>> list = getReplaceIndex(source, regex, replacement);
                    if (null != list && list.size() > 0) {
                        dest = replaceCustomAfter(source, list, replacement);
                    }
                } else {
                    String[] arr = source.split(regex);
                    for (String s : arr) {
                        dest += replaceBlankAll(s);
                    }
                }
            }
        }
        return dest;
    }

    /**
     * 计算替换字符的开始和结束索引位置
     *
     * @param source
     * @param regex
     * @param replacement
     * @return
     * @author wangmingchang
     * @date 2019/1/29 9:48
     **/
    private static List<Map<String, Integer>> getReplaceIndex(String source, String regex, String replacement) {
        List<Map<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        boolean startFlag = false;
        boolean endFlag = false;
        char[] sourceArr = source.toCharArray();
        char[] regexArr = regex.toCharArray();
        int lastIndex = regex.length() - 1;
        int j = 0;

        for (int i = 0; i < sourceArr.length; i++) {
            char c = sourceArr[i];
            if (c == regexArr[j] && j < regexArr.length) {
                if (j == 0) {
                    map.put(START_INDEX_KEY, i);
                    startFlag = true;
                    System.out.println("endIndex-->c:" + c + "  i:" + i);
                    if (regexArr.length == 1) {
                        endFlag = true;
                        map.put("endIndex", i);
                        System.out.println("endIndex-->c:" + c + "  i:" + i);
                    }
                } else if (j == lastIndex) {
                    endFlag = true;
                    map.put("endIndex", i);
                    System.out.println("endIndex-->c:" + c + "  i:" + i);
                }
                j++;
            } else {
                j = 0;
                startFlag = false;
                endFlag = false;
            }
            if (startFlag && endFlag) {
                list.add(map);
                map = new HashMap<>();
                startFlag = false;
                endFlag = false;
                j = 0;
            }
        }
        return list;
    }

    /**
     * 返回替换后的字符串
     *
     * @param source
     * @param replaceIndexs
     * @param replacement
     * @return
     * @author wangmingchang
     * @date 2019/1/29 9:47
     **/
    private static String replaceCustomAfter(String source, List<Map<String, Integer>> replaceIndexs, String replacement) {
        StringBuilder newSource = new StringBuilder();
        char[] sourceArr = source.toCharArray();
        for (int i = 0; i < sourceArr.length; i++) {
            Iterator<Map<String, Integer>> iterator = replaceIndexs.iterator();
            boolean flag = true;
            while (iterator.hasNext()) {
                Map<String, Integer> characterMap = iterator.next();
                Integer startIndex = characterMap.get(START_INDEX_KEY);
                Integer endIndex = characterMap.get(END_INDEX_KEY);
                if (i >= startIndex && i <= endIndex) {
                    flag = false;
                    if (i == startIndex) {
                        newSource.append(replacement);
                        if (startIndex.equals(endIndex)) {
                            iterator.remove();
                        }
                    } else if (i == endIndex) {
                        iterator.remove();
                    }
                }
                break;
            }
            if (flag) {
                newSource.append(sourceArr[i]);
            }
        }
        return newSource.toString();
    }

    /**
     * 去除自定义字符串
     *
     * @param source      字符串
     * @param regexs      替换字符数组
     * @param replacement 替换后字符
     * @return 没有换行的字符串
     */
    public static String replaceCustomBlank(String source, String[] regexs, String replacement) {
        String dest = "";
        List<Map<String, Integer>> replaceIndexs = new ArrayList<>();
        if (source != null) {
            for (String regex : regexs) {
                if (source.indexOf(regex) != -1) {
                    if (isNotBlank(replacement)) {
                        List<Map<String, Integer>> list = getReplaceIndex(source, regex, replacement);
                        if (null != list && list.size() > 0) {
                            replaceIndexs.addAll(list);
                        }
                    } else {
                        String[] arr = source.split(regex);
                        for (String s : arr) {
                            dest += replaceBlankAll(s);
                        }
                    }
                }
            }
            if (replaceIndexs.size() > 0) {
                Collections.sort(replaceIndexs, new Comparator<Map<String, Integer>>() {
                    @Override
                    public int compare(Map<String, Integer> o1, Map<String, Integer> o2) {
                        Integer startIndex1 = o1.get(START_INDEX_KEY);
                        Integer startIndex2 = o2.get(START_INDEX_KEY);
                        int num = startIndex1 - startIndex2;
                        if (num > 0) {
                            return 1;
                        } else if (num < 0) {
                            return -1;
                        }
                        //相等
                        return 0;
                    }
                });
                dest = replaceCustomAfter(source, replaceIndexs, replacement);

            }
        }
        return dest;
    }

    /**
     * 去除星标*
     *
     * @param str 字符串
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
     * 去除斜线（\/）
     *
     * @param str 字符串
     * @return 没有换行的字符串
     */
    public static String replaceSlashBlank(String str) {
        String dest = "";
        if (str != null) {
            String[] strArr = str.split("\\\\");
            String newStr = "";
            for (String s : strArr) {
                newStr += s;
            }
            String[] newStrArr = newStr.split("/");
            for (String ns : newStrArr) {
                dest += ns;
            }
            dest = replaceBlankAll(dest);
        }
        return dest;
    }


    /**
     * 判断String数组是否存在某个值
     *
     * @param arr         数组
     * @param targetValue 值
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
     * @param cls class
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
     *
     * @return boolean
     * @author wmc
     * @date 2019\1\20
     * @Param [str, arr]
     **/
    public static boolean indexOf(String str, String[] arr) {
        boolean flag = false;
        for (String s : arr) {
            if (str.indexOf(s) != -1) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 去除${}符号
     * @author wangmingchang
     * @date 2019/1/29 15:02
     * @param str
     * @return
     **/
    public static String removeSymbol(String str) {
        String[] arr = str.split("\\$\\{");
        if(arr.length > 1){
            arr = arr[1].split("}");
        }else {
            arr = arr[0].split("}");
        }
        return arr[0];

    }

    public static void main(String[] arg) {
//        String str = "SSabcfdFDeacFDxxXxxADVCFDFDOOOP";
//        String s = StringUtil.replaceCustomBlank(str, "x", "A");
//        String s2 = StringUtil.replaceCustomBlank(str, new String[]{"FD", "OOO", "xx", "S"}, "Zz");
//        System.out.println("str----------》" + str);
//        System.out.println("s----------》" + s);
//        System.out.println("s2----------》" + s2);


        String s3 = "${abc";
        System.out.println("s3----------》" + StringUtil.removeSymbol(s3));

    }
}
