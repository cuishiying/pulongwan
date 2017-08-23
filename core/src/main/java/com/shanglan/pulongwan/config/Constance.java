package com.shanglan.pulongwan.config;

import com.shanglan.pulongwan.entity.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuishiying on 2017/7/25.
 */
public class Constance {
    private static boolean receiveDataFlat = true;
    private static boolean handlerDataFlat = true;
//    private static String apollo_host = "tcp://localhost:61613";
    private static String apollo_host = "tcp://10.38.8.201:61613";
    private static String apollo_clientid = "pulongwan_clientid";
    private static int socket_port = 10000;

    private static HashMap fieldConfig = new HashMap();

    /**
     * topic  hash映射表
     * @param list
     */
    public static void setConfig(List<Field> list){
        for(int i=0;i<list.size();i++){
            fieldConfig.put(list.get(i).getTelemetrySignal(),list.get(i).getDescriber());
        }
    }
    public static HashMap getFieldConfig(){
        return fieldConfig;
    }


    public static boolean isReceiveDataFlat() {
        return receiveDataFlat;
    }

    public static void setReceiveDataFlat(boolean receiveDataFlat) {
        Constance.receiveDataFlat = receiveDataFlat;
    }

    public static boolean isHandlerDataFlat() {
        return handlerDataFlat;
    }

    public static void setHandlerDataFlat(boolean handlerDataFlat) {
        Constance.handlerDataFlat = handlerDataFlat;
    }

    public static String getApollo_host() {
        return apollo_host;
    }

    public static void setApollo_host(String apollo_host) {
        Constance.apollo_host = apollo_host;
    }

    public static String getApollo_clientid() {
        return apollo_clientid;
    }

    public static void setApollo_clientid(String apollo_clientid) {
        Constance.apollo_clientid = apollo_clientid;
    }

    public static int getSocket_port() {
        return socket_port;
    }

    public static void setSocket_port(int socket_port) {
        Constance.socket_port = socket_port;
    }

    public static void setFieldConfig(HashMap fieldConfig) {
        Constance.fieldConfig = fieldConfig;
    }

    public static String getFieldConfig(String key){
        String value = (String) fieldConfig.get(key);
        return value;
    }
}
