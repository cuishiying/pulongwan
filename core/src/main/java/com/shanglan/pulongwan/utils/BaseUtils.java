package com.shanglan.pulongwan.utils;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BaseUtils {

    private static boolean logTag = true;
    private static Gson gson;

    /**
     * 日期转字符串
     * @param data
     * @param format
     * @return
     */
    public static String date2String(Date data, String format){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     * @param str
     * @return
     */
    public static LocalDateTime string2Date(String str){
        Date ts = null;
        SimpleDateFormat myFmt1=new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        try {
            ts = myFmt1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Instant instant = Instant.ofEpochMilli(ts.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    /**
     * 调试日志
     * @param msg
     */
    public static void debug(Object msg){
        if(logTag){
            if(msg instanceof String){
                System.out.println(msg);
            }else if(msg instanceof Integer){
                System.out.println(String.valueOf(msg));
            }else{
                System.out.println(createGsonString(msg));
            }
        }
    }

    /**
     * 对象转json
     * @param object
     * @return
     */
    public static String createGsonString(Object object) {
        if(null==gson){
            gson = new Gson();
        }
        String gsonString = gson.toJson(object);
        return gsonString;
    }

    /**
     * json转对象
     * @param gsonString
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        if(null==gson){
            gson = new Gson();
        }
        T t = gson.fromJson(gsonString, cls);
        return t;
    }
}
