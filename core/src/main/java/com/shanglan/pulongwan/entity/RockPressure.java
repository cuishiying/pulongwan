package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;
import com.shanglan.pulongwan.utils.BaseUtils;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 矿压
 */
@Entity
public class RockPressure extends BaseEntity{


    private static final long serialVersionUID = 1358532952692556598L;

    private String mainPoint;//主站
    private String reportWay;//通讯方式
    private String subPoint;//分站
    private String subPointType;//分站类型
    private String sensorRegion;//传感器安装区域
    private String sensorCode;//传感器编号   001
    private String sensorType;//传感器类型   综采压力
    private String oneValue;//一通道测量值
    private String twoValue;//二通道测量值
    private String threeValue;//三通道测量值
    private String unit;//单位
    private String reportStatus;//通信状态
    private LocalDateTime time;//时间
    private LocalDateTime delTime;

    public RockPressure() {
    }
    public RockPressure(HashMap<String,String> map) {
        setMainPoint(map.get("mainPoint"));
        setSubPoint(map.get("subPoint"));
        setSensorCode(map.get("sensorCode"));
        setSensorRegion(map.get("sensorRegion"));
        setUnit(map.get("unit"));
        setOneValue(map.get("oneValue"));
        setTwoValue(map.get("twoValue"));
        setReportWay("在线");
        setSubPointType("综采监测");
        setSensorType("综采压力");
        setTime(BaseUtils.string2Date(map.get("time")));
    }


    public String getMainPoint() {
        return mainPoint;
    }

    public void setMainPoint(String mainPoint) {
        this.mainPoint = mainPoint;
    }

    public String getReportWay() {
        return reportWay;
    }

    public void setReportWay(String reportWay) {
        this.reportWay = reportWay;
    }

    public String getSubPoint() {
        return subPoint;
    }

    public void setSubPoint(String subPoint) {
        this.subPoint = subPoint;
    }

    public String getSubPointType() {
        return subPointType;
    }

    public void setSubPointType(String subPointType) {
        this.subPointType = subPointType;
    }

    public String getSensorRegion() {
        return sensorRegion;
    }

    public void setSensorRegion(String sensorRegion) {
        this.sensorRegion = sensorRegion;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getOneValue() {
        return oneValue;
    }

    public void setOneValue(String oneValue) {
        this.oneValue = oneValue;
    }

    public String getTwoValue() {
        return twoValue;
    }

    public void setTwoValue(String twoValue) {
        this.twoValue = twoValue;
    }

    public String getThreeValue() {
        return threeValue;
    }

    public void setThreeValue(String threeValue) {
        this.threeValue = threeValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getDelTime() {
        return delTime;
    }

    public void setDelTime(LocalDateTime delTime) {
        this.delTime = delTime;
    }
}
