package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Entity;

/**
 * Created by cuishiying on 2017/5/10.
 */
@Entity
public class Topic extends BaseEntity{

    private static final long serialVersionUID = -7594951171340265653L;

    private String monitorName;//监控项名称（1号风机）
    private String topic;//订阅主题

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
