package com.shanglan.pulongwan.thread;

import com.shanglan.pulongwan.config.Constance;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;


/**
 * Created by cuishiying on 2017/7/25.
 */
@Service
@Transactional
public class ThreadUtils {


    private AsyncTaskExecutor executor;

    private ReceiveThread receiveThread;
    private HandleThread handleThread;

    private Lock lock = new Lock();
    private Queue queue = new Queue();

    public void start(){
        executor = new SimpleAsyncTaskExecutor("udp");
        receiveThread = new ReceiveThread(lock,queue);
        handleThread = new HandleThread(lock,queue);
        executor.submit(receiveThread);
        executor.submit(handleThread);
    }

    public void stopThread() throws MqttException {
        this.stopHandler();
        this.stopReceive();
    }

    public void stopReceive(){
        Constance.setReceiveDataFlat(false);
        if(null!=receiveThread){
            receiveThread.interrupt();
        }
    }

    public void stopHandler() throws MqttException {
        if(null!=handleThread){
            MqttUtils.closeServer();
            handleThread.interrupt();
        }
    }

}
