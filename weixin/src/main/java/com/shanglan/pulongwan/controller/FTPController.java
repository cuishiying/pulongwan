package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.service.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/ftp")
public class FTPController {

    String filePath = "/Users/cuishiying/2017/04/bk/src/oa/矿压/";


    @Autowired
    private FTPService ftpService;

    /**
     * 矿压监测页面
     * @return
     */
    @RequestMapping(path = "/rockPressure", method = RequestMethod.GET)
    public ModelAndView RockPressureView(){
        ModelAndView model = new ModelAndView("rockPressure");
        List<RockPressure> rockPressures = ftpService.initRockPressureData();
        model.addObject("list",rockPressures);
        return model;
    }
}
