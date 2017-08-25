package com.shanglan.pulongwan.thread;

import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.interf.OnHandleDataListener;
import com.shanglan.pulongwan.config.Constance;
import com.shanglan.pulongwan.mqtt.ServerMQTT;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.util.Strings;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by cuishiying on 2017/7/25.
 */
@Component
@Scope
public class HandleThread extends Thread {

    private Lock lock;
    private Queue queue;

    public HandleThread(){}

    public HandleThread(Lock lock) {
        this.lock = lock;
    }

    public HandleThread(Lock lock,Queue queue) {
        this.lock = lock;
        this.queue = queue;
    }
    /**
     * 解析数据
     * 循环判断队列2操作标志
     * false：解析数据、发布消息
     * true：休眠一段时间
     */
    @Override
    public void run() {
        queue.setOnHandlerData(new OnHandleDataListener() {
            @Override
            public void handleData(List<Field> data) {
                MqttUtils.publishData(data);
            }
        });

        try{
            ServerMQTT.getInstance().connectWithResult();
            while (true){
                if(!lock.isLock()){
                    queue.pullData();
                    lock.setLock(true);
                }else{
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!Constance.isHandlerDataFlat()){
                    Thread.interrupted();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
