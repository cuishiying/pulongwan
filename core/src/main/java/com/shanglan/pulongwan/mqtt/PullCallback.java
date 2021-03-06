package com.shanglan.pulongwan.mqtt; /**
 *
 * Description:
 * @author admin
 * 2017年2月10日下午18:04:07
 */

import com.shanglan.pulongwan.utils.MqttUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 发布消息的回调类
 *
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 *
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 *
 *  public void connectionLost(Throwable cause)在断开连接时调用。
 *
 *  public void deliveryComplete(MqttDeliveryToken token))
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 *  由 MqttClient.connect 激活此回调。
 *
 */
public class PullCallback implements MqttCallback {

    private ClientMQTT clientMQTT;
    private int count = 0;



    public PullCallback(ClientMQTT serverMQTT) throws MqttException {
        this.clientMQTT = serverMQTT;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
//        System.out.println("连接断开，可以做重连");
        int timeout = 4000;
        try{
            while (count<10){
                long currentTime = System.currentTimeMillis();
                if(!clientMQTT.getClient().isConnected()){
                    clientMQTT.reconnect();
                }
                long now = System.currentTimeMillis();
                if((currentTime + timeout) < now){
                    break;
                }
                Thread.sleep(500);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        MqttUtils.saveData(topic,new String(message.getPayload(),"UTF-8"));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}