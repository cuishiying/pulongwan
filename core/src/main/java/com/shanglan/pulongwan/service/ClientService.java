package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.mqtt.ClientMQTT;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by cuishiying on 2017/5/10.
 */
@Service
@Transactional
public class ClientService {

    private ClientMQTT clientMQTT;

    @Autowired
    private ManageService manageService;

    /**
     * 开始订阅
     * @return
     */
    public AjaxResponse startClientSubscribe(){
        clientMQTT = new ClientMQTT();
        clientMQTT.setTopic("35kv/#").start(new MqttCallback() {
            public void connectionLost(Throwable throwable) {
                System.out.println("Client connectionLost");
                if (!clientMQTT.getClient().isConnected()) {
                    try {
                        clientMQTT.getClient().connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("接收消息主题 : " + topic);
                System.out.println("接收消息Qos : " + message.getQos());
                System.out.println("接收消息内容 : " + new String(message.getPayload(),"UTF-8"));
                manageService.handleData(topic,new String(message.getPayload(),"UTF-8"));
            }

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
            }
        });
        return AjaxResponse.success();
    }

    /**
     * 取消订阅
     * @return
     */
    public AjaxResponse stopClientSubscribe(){
        clientMQTT.stop();
        return AjaxResponse.success();
    }

}
