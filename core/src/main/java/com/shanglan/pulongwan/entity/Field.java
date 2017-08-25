package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Entity;

/**
 * Created by cuishiying on 2017/7/24.
 */
@Entity
public class Field extends BaseEntity{
    private static final long serialVersionUID = 6989773660193333819L;


    private String telemetrySignal;//遥测号
    private String describer;//描述
    private String type;//类型
    private Float ratio;//系数
    private String frameType;//帧类别
    private Integer dataCount;//信息数
    private String functionCode;//功能码
    private String srcValue;//源码值
    private Float telemetryValue;//遥测值
    private Integer topTopic; //一级主题
    private Integer warmingValue;//警戒值

    public String getTelemetrySignal() {
        return telemetrySignal;
    }

    public void setTelemetrySignal(String telemetrySignal) {
        this.telemetrySignal = telemetrySignal;
    }

    public String getDescriber() {
        return describer;
    }

    public void setDescriber(String describer) {
        this.describer = describer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getSrcValue() {
        return srcValue;
    }

    public void setSrcValue(String srcValue) {
        this.srcValue = srcValue;
    }

    public Float getTelemetryValue() {
        return telemetryValue;
    }

    public void setTelemetryValue(Float telemetryValue) {
        this.telemetryValue = telemetryValue;
    }

    public Integer getTopTopic() {
        return topTopic;
    }

    public void setTopTopic(Integer topTopic) {
        this.topTopic = topTopic;
    }

    public Integer getWarmingValue() {
        return warmingValue;
    }

    public void setWarmingValue(Integer warmingValue) {
        this.warmingValue = warmingValue;
    }
}
