package com.shanglan.pulongwan.utils;

import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.interf.OnSaveMqttDataListener;
import com.shanglan.pulongwan.mqtt.ServerMQTT;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.util.Strings;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MqttUtils {
    /**
     * 3.5kv,一个主题发布一条消息
     * @param data
     */
    public static void publishData(List<Field> data){
        try {
            if(data!=null&&data.size()>0){
                for(int i=0;i<data.size();i++){
                    String topic = data.get(i).getDescriber();
                    Float message = data.get(i).getTelemetryValue();
                    if(StringUtils.isEmpty(topic)|| Strings.containsAny(topic, "#+")){
                        continue;
                    }
                    publish(topic,message+"");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布矿压主题
     * 主题：传感器编码
     * 内容：所有数据转json
     * @param data
     */
    public static void publishRockPressure(List<RockPressure> data){
        BaseUtils.debug(data.size());
        try {
            if(data!=null&&data.size()>0){
                for(int i=0;i<data.size();i++){
                    String topic = data.get(i).getSensorCode();
                    RockPressure rockPressure = data.get(i);
                    String message = BaseUtils.createGsonString(rockPressure);
                    if(StringUtils.isEmpty(topic)|| Strings.containsAny(topic, "#+")){
                        continue;
                    }
                    publish(topic,message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布主题
     * @param topic
     * @param data
     * @throws MqttException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static void publish(String topic,String data) throws MqttException, InterruptedException, UnsupportedEncodingException {
        BaseUtils.debug("=====publish====isConnected====="+ServerMQTT.getInstance().isConnected()+"====topic===="+topic+"====data===="+data);
        if(ServerMQTT.getInstance().isConnected()){
            ServerMQTT.getInstance().publish(topic,data);
        }else{
            ServerMQTT.getInstance().reconnect();
        }
    }

    public static void saveData(String topic,String data){
        if(null!=onSaveMqttDataListener){
            onSaveMqttDataListener.save(topic,data);
        }
    }

    public static void closeServer() throws MqttException {
        ServerMQTT.getInstance().closeServer();
    }

    private static OnSaveMqttDataListener onSaveMqttDataListener;
    public static void setOnSaveMqttDataListener(OnSaveMqttDataListener l){
        onSaveMqttDataListener = l;
    };
}
