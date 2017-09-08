package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * Created by cuishiying on 2017/5/12.
 */
@Entity
public class TopicDetail extends BaseEntity{

    private static final long serialVersionUID = -8206225398536914424L;

    private String topic;
    private Integer topicId;
    private Double monitorValue;
    private LocalDateTime delTime;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Double getMonitorValue() {
        return monitorValue;
    }

    public void setMonitorValue(Double monitorValue) {
        this.monitorValue = monitorValue;
    }

    public LocalDateTime getDelTime() {
        return delTime;
    }

    public void setDelTime(LocalDateTime delTime) {
        this.delTime = delTime;
    }
}
