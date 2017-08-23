package com.shanglan.pulongwan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by cuishiying on 2017/3/22.
 */
@RestController
@RequestMapping("/index")
public class IndexController {



    /**
     * 返回数据到模板页面
     *
     * @return
     */
    @RequestMapping
    public ModelAndView index() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("name", "test");
        return model;
    }
}
