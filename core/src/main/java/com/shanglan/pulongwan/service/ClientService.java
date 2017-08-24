package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.mqtt.ClientMQTT;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by cuishiying on 2017/5/10.
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private ManageService manageService;

    /**
     * 开始订阅
     * @return
     */
    public ClientService startClientSubscribe() throws MqttException, UnsupportedEncodingException {

//        ClientMQTT.getInstance().setCallBack(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                System.out.println("接收消息主题 : " + topic);
//                System.out.println("接收消息Qos : " + message.getQos());
//                System.out.println("接收消息内容 : " + new String(message.getPayload(),"UTF-8"));
//                manageService.handleData(topic,new String(message.getPayload(),"UTF-8"));
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//                System.out.println("deliveryComplete---------" + token.isComplete());
//            }
//        }).connectWithResult().subscribeTopic("10KV/#").subscribeTopic("2号风机/#").subscribeTopic("1号风机/#");
        return this;
    }

    /**
     * 取消订阅
     * @return
     */
    public AjaxResponse stopClientSubscribe() throws MqttException, UnsupportedEncodingException {
//        ClientMQTT.getInstance().stop();
        return AjaxResponse.success();
    }

}
