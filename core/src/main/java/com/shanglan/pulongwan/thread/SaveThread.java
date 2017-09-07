package com.shanglan.pulongwan.thread;

import com.shanglan.pulongwan.mqtt.ClientMQTT;
import com.shanglan.pulongwan.mqtt.PullCallback;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class SaveThread extends Thread {

    @Override
    public void run() {
//        try {
//            ClientMQTT.getInstance().connectWithResult().subscribeTopic("35KV/#").subscribeTopic("10KV/#").subscribeTopic("1号风机/#").subscribeTopic("2号风机/#").setCallBack(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) {
//                    System.out.println("要断线重连");
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                    MqttUtils.saveData(topic,new String(message.getPayload(),"UTF-8"));
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {
//
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        try {
            ClientMQTT instance = ClientMQTT.getInstance();
            instance.connectWithResult().subscribeTopic("35KV/#").subscribeTopic("10KV/#").subscribeTopic("1号风机/#").subscribeTopic("2号风机/#").setCallBack(new PullCallback(instance));
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
