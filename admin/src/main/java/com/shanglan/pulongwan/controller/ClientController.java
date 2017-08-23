package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.mqtt.ClientMQTT;
import com.shanglan.pulongwan.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public AjaxResponse startClientSubscribe(){
        AjaxResponse ajaxResponse = clientService.startClientSubscribe();
        return ajaxResponse;
    }

    /**
     * 取消订阅
     * @return
     */
    @RequestMapping(path = "/stop",method = RequestMethod.GET)
    public AjaxResponse stopClientSubscribe(){
        AjaxResponse ajaxResponse = clientService.stopClientSubscribe();
        return ajaxResponse;
    }




}
