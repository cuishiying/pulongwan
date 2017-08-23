package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Connection;

/**
 * Created by cuishiying on 2017/3/22.
 * 导入主题映射表
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private FieldService fieldService;

    @RequestMapping(path = "/import", method = RequestMethod.GET)
    public ModelAndView importView(){
        ModelAndView model = new ModelAndView("import");
        return model;
    }

    @RequestMapping(path = "/import", method = RequestMethod.POST)
    public AjaxResponse<?> importExcel(HttpServletRequest request) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        InputStream in = file.getInputStream();
        AjaxResponse ajaxResponse = fieldService.importService(in, file);
        return ajaxResponse;
    }

}
