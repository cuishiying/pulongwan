package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.repository.ManageRepository;
import com.shanglan.pulongwan.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by cuishiying on 2017/5/10.
 * 主题订阅管理
 */
@RestController
@RequestMapping("/")
public class ManageController {

    @Autowired
    private ManageService manageService;



    /**
     * 主题列表页
     * @return
     */
    @RequestMapping
    public ModelAndView index(@PageableDefault Pageable pageable){
        ModelAndView model = new ModelAndView("page1");
        Page<Topic> page = manageService.getTopics(pageable);
        model.addObject("page",page);
        model.addObject("topic",page.getContent().get(0));
        return model;
    }

    /**
     * 主题详情页(展示数据详情页)
     * @return
     */
    @RequestMapping(path = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView deployDetailView(@PathVariable("id") Integer topicId){
        ModelAndView model = new ModelAndView("page1");
        Topic topic = manageService.getTopic(topicId);
        model.addObject("page",manageService.getTopics(null));
        model.addObject("topic",topic);
        return model;
    }

    /**
     * 主题详情(展示图表详情页)
     * @return
     */
    @RequestMapping(path = "/chart/{id}",method = RequestMethod.GET)
    public ModelAndView chartView(@PathVariable("id") Integer dataId){
        ModelAndView model = new ModelAndView("chart_line");
        Topic topic = manageService.getTopic(dataId);
        model.addObject("topic",topic);
        model.addObject("page",manageService.getTopics(null));

        return model;
    }
}
