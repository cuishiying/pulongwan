package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.mqtt.ClientMQTT;
import com.shanglan.pulongwan.service.ClientService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * Created by cuishiying on 2017/5/10.
 * 启动mqtt服务
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 开始订阅
     * @return
     */
    @RequestMapping(path = "/start",method = RequestMethod.GET)
    public AjaxResponse startClientSubscribe() throws MqttException, UnsupportedEncodingException {
        clientService.startClientSubscribe();
        return null;
    }

    /**
     * 取消订阅
     * @return
     */
    @RequestMapping(path = "/stop",method = RequestMethod.GET)
    public AjaxResponse stopClientSubscribe() throws MqttException, UnsupportedEncodingException {
        clientService.stopClientSubscribe();
        return null;
    }




}
