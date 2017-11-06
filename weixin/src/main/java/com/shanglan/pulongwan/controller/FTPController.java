package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.dto.RockPressureQueryDTO;
import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.service.FTPService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ftp")
public class FTPController {

    String filePath = "/Users/cuishiying/2017/04/bk/src/oa/矿压监测/";


    @Autowired
    private FTPService ftpService;

    /**
     * 矿压监测页面
     * @return
     */
    @RequestMapping(path = "/rockPressure", method = RequestMethod.GET)
    public ModelAndView RockPressureView(RockPressureQueryDTO queryDTO, @PageableDefault Pageable pageable, HttpServletRequest request){
        ModelAndView model = new ModelAndView("rockPressure");

        List<RockPressure> rockPressures = ftpService.initRockPressureData();

        String uid = (String) request.getSession().getAttribute("uid");
        if(StringUtils.isEmpty(uid)){
            request.getSession().invalidate();
            request.getSession().setAttribute("uid","uid"+String.valueOf( Math.random()).hashCode());
            uid = (String) request.getSession().getAttribute("uid");
        }

        Topic topic = new Topic("矿压监测","矿压监测");


        model.addObject("list",rockPressures);
        model.addObject("topic",topic);
        model.addObject("uid",uid);
        return model;
    }
    @RequestMapping(path = "/rockPressure/history", method = RequestMethod.GET)
    public ModelAndView RockPressureHistoryView(RockPressureQueryDTO queryDTO, @PageableDefault Pageable pageable,HttpServletRequest request){
        ModelAndView model = new ModelAndView("rockPressure_history");
        Page<RockPressure> rockPressures = ftpService.findRockPressureData(queryDTO,pageable);
        model.addObject("page",rockPressures);
        model.addObject("queryDTO",queryDTO);
        return model;
    }
}
