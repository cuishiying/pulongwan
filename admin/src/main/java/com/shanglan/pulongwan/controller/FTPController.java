package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.dto.RockPressureQueryDTO;
import com.shanglan.pulongwan.entity.FTPConf;
import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.service.FTPService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView RockPressureView(RockPressureQueryDTO queryDTO, @PageableDefault Pageable pageable,HttpServletRequest request){
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
        return model;
    }

    @RequestMapping(path = "/confs", method = RequestMethod.GET)
    public ModelAndView ftpList(){
        ModelAndView model = new ModelAndView("ftp_list");
        List<FTPConf> list = ftpService.findAllConf();
        model.addObject("list",list);
        return model;
    }

    @RequestMapping(path = "/conf/update/{id}",method = RequestMethod.GET)
    public ModelAndView updateConf(@PathVariable Integer id){
        ModelAndView model = new ModelAndView("ftp_conf");
        FTPConf conf = ftpService.findFTPConfById(id);
        model.addObject("conf",conf);
        return model;
    }
    @RequestMapping(path = "/conf/update",method = RequestMethod.POST)
    public AjaxResponse updateConf(@RequestParam String name,@RequestParam String ftppath,@RequestParam String monitorfile ){
        AjaxResponse ajaxResponse = ftpService.updateFTPConf(name, ftppath, monitorfile);
        return ajaxResponse;
    }

    @RequestMapping(path = "/start/{id}",method = RequestMethod.GET)
    public AjaxResponse startFTP(@PathVariable Integer id) throws Exception {
        AjaxResponse ajaxResponse = ftpService.startFTP(id);
        return ajaxResponse;
    }

    @RequestMapping(path = "/stop/{id}",method = RequestMethod.GET)
    public AjaxResponse stopFTP(@PathVariable Integer id) throws Exception {
        AjaxResponse ajaxResponse = ftpService.stopFTP(id);
        return ajaxResponse;
    }

}
