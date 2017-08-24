package com.shanglan.pulongwan.mqtt; /**
 *
 * Description:
 * @author admin
 * 2017年2月10日下午17:50:15
 */

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ScheduledExecutorService;

public class ClientMQTT {

    private static ClientMQTT instance;


    public static final String HOST = "tcp://10.38.8.201:61613";
    private static final String clientid = "pulongwan_pc_clientid";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";


    public ClientMQTT() throws MqttException, UnsupportedEncodingException {
        // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        // MQTT的连接设置
        options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名
        options.setUserName(userName);
        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);

    }
    // 设置回调
    public ClientMQTT setCallBack(MqttCallback callBack){
        client.setCallback(callBack);
        return this;
    }

    public static synchronized ClientMQTT getInstance() throws MqttException, UnsupportedEncodingException {
        if (instance == null) {
            instance = new ClientMQTT();
        }
        return instance;
    }

    /**
     *  用来连接服务器
     */
    public void connect() throws MqttException {
        if (!client.isConnected()) {
            client.connect(options);
            if (client.isConnected()) {
                System.out.println("ClientMQTT Connected to MQTT Broker.");
            }
        }
    }
    public ClientMQTT connectWithResult() throws MqttException {
        IMqttToken iMqttToken = client.connectWithResult(options);
        System.out.println("mqttconnect"+iMqttToken.isComplete());
        return this;
    }
    public void reconnect() throws MqttException, InterruptedException {
        long currentTime = System.currentTimeMillis();
        int timeout = 4000;
        while (!client.isConnected()){
            client.reconnect();
            long now = System.currentTimeMillis();
            if((currentTime + timeout) < now){
                break;
            }
            Thread.sleep(500);
        }
    }

    public ClientMQTT subscribeTopic(String topic) throws MqttException {
        //订阅消息
        int[] Qos  = {0};
        String[] topic1 = {topic};
        client.subscribe(topic1, Qos);
        return this;
    }

    public void stop(){

    }
    public void stop(String topic){
        String[] topic1 = {topic};
        try {
            client.unsubscribe(topic1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getClient() {
        return client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }
}
