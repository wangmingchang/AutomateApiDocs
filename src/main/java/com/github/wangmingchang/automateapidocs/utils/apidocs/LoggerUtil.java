package com.github.wangmingchang.automateapidocs.utils.apidocs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description
 * @auther: wangmingchang
 * @date: 2019/2/12 11:30
 */
public class LoggerUtil {

    public static void info(String message){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormat = simpleDateFormat.format(new Date());
        System.out.println(dateFormat+"[ INFO ]"+ message);
    }
    public static void error(String message, Throwable t){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormat = simpleDateFormat.format(new Date());
        System.err.println(dateFormat+"[ ERROR ]"+ message);
        if(t != null) {
            t.printStackTrace();
        }
    }
}
