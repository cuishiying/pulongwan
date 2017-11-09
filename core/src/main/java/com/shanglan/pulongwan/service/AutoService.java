package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.config.Constance;
import com.shanglan.pulongwan.interf.OnSaveMqttDataListener;
import com.shanglan.pulongwan.thread.ThreadUtils;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;


/**
 * Created by cuishiying on 2017/5/10.
 */
@Service
@Transactional
public class AutoService {

    @Autowired
    private FieldService fieldService;
    @Autowired
    private FTPService ftpService;
    @Autowired
    private ManageService manageService;
    @Autowired
    private ClientService clientService;

    ThreadUtils threadUtils = new ThreadUtils();

    String filePath = "/Users/cuishiying/2017/04/bk/src/oa/矿压监测/";
    String fileName = "dev.txt";


    /**
     * 启动udp监听
     * @return
     * @throws Exception
     */
    @PostConstruct
    public AjaxResponse setMqttConfig() throws Exception {
        List<Field> all = fieldService.findAll();
        Constance.setConfig(all);//hash映射
        return AjaxResponse.success();
    }

    /**
     * 矿压监测
     * @throws Exception
     */
    @PostConstruct
    public void ftpStart() throws Exception {
        ftpService.monitorRockPressureFile(filePath,fileName);

    }


    /**
     * mqtt连接
     * 程序启动自动执行
     */
    @PostConstruct
    public void connectMqtt(){
        threadUtils.start();
    }

    /**
     * 释放线程资源
     * 程序停止前执行
     */
    @PreDestroy
    public void closeThread() throws MqttException {
        System.out.println("====closeThread====");
        threadUtils.stopThread();
    }

    /**
     * 自动存储监控数据
     */
    @PostConstruct
    public void saveMqttData() throws MqttException {
        MqttUtils.setOnSaveMqttDataListener(new OnSaveMqttDataListener() {
            @Override
            public void save(String topic, String data) {
                try {
                    manageService.handleData(topic,data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
