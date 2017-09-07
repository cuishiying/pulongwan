package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.repository.DataRepository;
import com.shanglan.pulongwan.repository.ManageRepository;
import com.shanglan.pulongwan.utils.TopicDetailPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuishiying on 2017/5/10.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageService {

    @Autowired
    private ManageRepository manageRepository;
    @Autowired
    private DataRepository dataRepository;


    private List<TopicDetail> tempData = new ArrayList<>(1024);


    /**
     * 获取主题列表
     * @param pageable
     * @return
     */
    public Page<Topic> getTopics(Pageable pageable){
        Page<Topic> customer = manageRepository.findAll(pageable);
        return customer;
    }

    public Topic getTopic(Integer id){
        Topic topic = manageRepository.findById(id);
        return topic;
    }

    /**
     * 添加订阅主题
     * @param topic
     * @return
     */
    public AjaxResponse addTopic(Topic topic){
        Topic topic1 = new Topic();
        topic1.setTopic("/home/temperature");
        topic1.setMonitorName("风机1号");
        manageRepository.save(topic1);
        return AjaxResponse.success("添加成功");
    }

    /**
     * 修改主题
     * @param topic
     * @return
     */
    public AjaxResponse updateTopic(Topic topic){
        Topic t = manageRepository.findById(topic.getId());
        t.setMonitorName(topic.getMonitorName());
        t.setTopic(topic.getTopic());
        return AjaxResponse.success("修改成功");
    }

    /**
     * 删除主题
     * @param id
     * @return
     */
    public AjaxResponse deleteTopic(Integer id){
        manageRepository.delete(id);
        return AjaxResponse.success("删除成功");
    }


    /**
     * 处理订阅数据
     * @param list
     * @return
     */
    public AjaxResponse handleData(List<TopicDetail> list){

        //每分钟存储一次
        if(tempData.size()<60){
            tempData.addAll(list);
        }else{
            long startTime=System.currentTimeMillis();//记录开始时间
            dataRepository.save(tempData);
            long endTime=System.currentTimeMillis();//记录结束时间
            float excTime=(float)(endTime-startTime)/1000;
            System.out.println("执行时间："+excTime+"s");
            System.out.println("size=="+dataRepository.findAll().size());
            tempData.clear();
        }

        return AjaxResponse.success();
    }

    /**
     * 保存历史数据
     * @param topic
     * @param message
     * @return
     * @throws Exception
     */
    public AjaxResponse handleData(String topic,String message) throws Exception {

        //每60条数据存储一次
        TopicDetail topicDetail = new TopicDetail();
        topicDetail.setTopic(topic);
        topicDetail.setMonitorValue(message);
        topicDetail.setDelTime(LocalDateTime.now());
        tempData.add(topicDetail);
        topicDetail = null;

        if(tempData.size()>=60){
            dataRepository.save(tempData);
            tempData.clear();
        }

//        TopicDetail topicDetail = TopicDetailPoolFactory.borrowObject();
//        topicDetail.setTopic(topic);
//        topicDetail.setMonitorValue(message);
//        topicDetail.setDelTime(LocalDateTime.now());
//        dataRepository.save(topicDetail);
//        topicDetail.setId(null);
//        TopicDetailPoolFactory.returnObject(topicDetail);
        return AjaxResponse.success();
    }
}
