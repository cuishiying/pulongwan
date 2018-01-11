package com.shanglan.pulongwan.controller;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.config.Constance;
import com.shanglan.pulongwan.dto.QueryDTO;
import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.service.AutoService;
import com.shanglan.pulongwan.service.DbService;
import com.shanglan.pulongwan.service.FieldService;
import com.shanglan.pulongwan.service.ManageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AutoService autoService;

    @Autowired
    private DbService dbService;

    @Autowired
    private FieldService fieldService;


    /**
     * 主题列表页
     * @return
     */
    @RequestMapping
    public ModelAndView deployView(String username,String truename,@PageableDefault Pageable pageable,HttpServletRequest request) throws Exception {
        if(StringUtils.isNotEmpty(username)&&StringUtils.isNotEmpty(truename)){
            request.getSession().invalidate();
            request.getSession().setAttribute("uid", "uid"+username.hashCode()+String.valueOf( Math.random()).hashCode());
        }else{
            request.getSession().invalidate();
            request.getSession().setAttribute("uid","uid"+ Constance.getUUid());
        }
        ModelAndView model = new ModelAndView("topicList");
        Page<Topic> page = manageService.getTopics(pageable);

        model.addObject("page",page);
        return model;
    }

    /**
     * 主题详情页(展示数据详情页)
     * 前端根据一级主题topic订阅
     * @return
     */
    @RequestMapping(path = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView deployDetailView(@PathVariable("id") Integer topicId,HttpServletRequest request) throws Exception{

        String uid = (String) request.getSession().getAttribute("uid");
        if(StringUtils.isEmpty(uid)){
            request.getSession().invalidate();
            request.getSession().setAttribute("uid","uid"+String.valueOf( Math.random()).hashCode());
            uid = (String) request.getSession().getAttribute("uid");
        }

        ModelAndView model = new ModelAndView("topicDetail");
        Topic topic = manageService.getTopic(topicId);//一级主题
        List<Field> fields = fieldService.findAll(topicId);

        model.addObject("topic",topic);
        model.addObject("fields",fields);
        model.addObject("uid",uid);
        return model;
    }

    /**
     * 历史数据
     * @param id
     * @param queryDTO
     * @return
     */
    @RequestMapping(path = "/history/{id}",method = RequestMethod.GET)
    public ModelAndView historyView(@PathVariable("id") Integer id,QueryDTO queryDTO){
        ModelAndView model = new ModelAndView("historyView");
        List<TopicDetail> historyData = dbService.findHistoryData(id, queryDTO);
        model.addObject("topicId",id);
        model.addObject("queryDTO",queryDTO);
        model.addObject("historyData",historyData);
        return model;
    }

    /**
     * 主题详情(展示图表详情页)
     * @return
     */
    @RequestMapping(path = "/chart/{id}",method = RequestMethod.GET)
    public ModelAndView chartView(@PathVariable("id") Integer id,HttpServletRequest request){
        String uid = (String) request.getSession().getAttribute("uid");
        if(StringUtils.isEmpty(uid)){
            request.getSession().invalidate();
            request.getSession().setAttribute("uid","uid"+String.valueOf( Math.random()).hashCode());
            uid = (String) request.getSession().getAttribute("uid");
        }
        ModelAndView model = new ModelAndView("chartView_line");
        Field topic = fieldService.findById(id);
        model.addObject("topic",topic.getDescriber());
        model.addObject("uid",uid);
        return model;
    }

    /**
     * 添加主题页
     * @return
     */
    @RequestMapping(path = "/toAdd",method = RequestMethod.GET)
    public ModelAndView toAdd(){
        ModelAndView model = new ModelAndView("topic_add");
        return model;
    }
    /**
     * 修改主题页
     * @return
     */
    @RequestMapping(path = "/toUpdate",method = RequestMethod.GET)
    public ModelAndView toUpdate(){
        ModelAndView model = new ModelAndView("topic_update");
        return model;
    }


    /**
     * 添加主题
     * @return
     */
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    public AjaxResponse addDeploy(@RequestBody Topic topic){
        AjaxResponse ajaxResponse = manageService.addTopic(null);
        return ajaxResponse;
    }

    /**
     * 修改主题
     * @return
     */
    @RequestMapping(path = "/update",method = RequestMethod.POST)
    public AjaxResponse updateDeploy(@RequestBody Topic topic){
        AjaxResponse ajaxResponse = manageService.updateTopic(topic);
        return ajaxResponse;
    }

    /**
     * 删除主题
     * @return
     */
    @RequestMapping(path = "/delete/{id}",method = RequestMethod.POST)
    public AjaxResponse deleteDeploy(@PathVariable("id") Integer topicId){
        return null;
    }
}
