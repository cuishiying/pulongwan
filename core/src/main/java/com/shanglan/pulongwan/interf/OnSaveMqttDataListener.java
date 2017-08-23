package com.shanglan.pulongwan.interf;

import com.shanglan.pulongwan.entity.TopicDetail;

import java.util.List;

public interface OnSaveMqttDataListener {
    void save(String topic,String data);
}
