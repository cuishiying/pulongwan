package com.shanglan.pulongwan.mqtt; /**
 * Created by Administrator on 17-2-10.
 */

import com.shanglan.pulongwan.config.Constance;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

/**
 *
 * Title:Server
 * Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author admin
 * 2017年2月10日下午17:41:10
 */
public class ServerMQTT {

    private static ServerMQTT instance;

    private MqttClient client;
    private MqttTopic mqttTopic;
    private String userName = "admin";
    private String passWord = "password";

    private MqttMessage message;
    private MqttConnectOptions options;

    /**
     * 构造函数
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(Constance.getApollo_host(), Constance.getApollo_clientid(), new MemoryPersistence());
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(100);
        // 设置会话心跳时间
        options.setKeepAliveInterval(10);
        options.setAutomaticReconnect(true);
        client.setCallback(new PushCallback(this));
        instance = this;
    }

    public static synchronized ServerMQTT getInstance() throws MqttException {
        if (instance == null) {
            instance = new ServerMQTT();
        }
        return instance;
    }

    /**
     *  用来连接服务器
     */
    public void connect() throws MqttException {
        if (!isConnected()) {
            client.connect(options);
            if (isConnected()) {
                System.out.println("Connected to MQTT Broker.");
            }
        }
    }
    public IMqttToken connectWithResult() throws MqttException {
        IMqttToken iMqttToken = client.connectWithResult(options);
        System.out.println("mqttconnect"+iMqttToken.isComplete());
        return iMqttToken;
    }
    public void reconnect() throws MqttException, InterruptedException {
        long currentTime = System.currentTimeMillis();
        int timeout = 4000;
        while (!isConnected()){
            client.reconnect();
            long now = System.currentTimeMillis();
            if((currentTime + timeout) < now){
                break;
            }
            Thread.sleep(500);
        }
    }


    /**
     * 组装发布消息
     * @param data
     * @throws MqttException
     */
    public void publish(String topic,String data) throws MqttException, UnsupportedEncodingException {
//        MqttTopic.validate(topic,true);
        if (topic.equals("")||data.equals("")) {
           return;
        }
        if(client.isConnected()){
            mqttTopic = client.getTopic(topic);
        }else{
            client.disconnect();
            return;
        }

        message = new MqttMessage();
        message.setQos(0);
        message.setRetained(true);
        message.setPayload(data.getBytes("UTF-8"));

        this.publish(mqttTopic , message);
    }

    /**
     *
     * @param topic
     * @param message
     * 发布消息
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        while (!token.isComplete()) {
            token.waitForCompletion();
        }
//        System.out.println("消息发布成功:==="+ token.isComplete()+"===topic===="+topic.getName()+"=====message==="+new String(message.getPayload()));
    }

    /**
     * 关闭发布
     */
    public void closeServer() throws MqttException {
        client.disconnect();
        client.close();
        while (client.isConnected()) {
            //等待断开
        }
        if (!isConnected()) {
            System.out.println("Disconnected from the MQTT Broker.");
        }
    }

    public boolean isConnected(){
        return client.isConnected();
    }

    public MqttClient getClient(){
        return client;
    }
}
