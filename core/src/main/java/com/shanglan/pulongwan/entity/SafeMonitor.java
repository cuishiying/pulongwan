package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;

/**
 * 安全监测
 */
@Entity
@Table(name = "cnoa_safe_monitor")
public class SafeMonitor extends BaseEntity{
    private static final long serialVersionUID = 5724599311459764306L;

    private String codeIndex;   //分站编号
    private String sensorCode;  //传感器编号
    private String sensorRegion;    //安装地点
    private String sensorValue;    //监测值
    private String name;   //名称
    private String sensorType;  //类型
    private String unit;   //单位
    private String rangeLower;   //量程下限
    private String rangeHigher;   //量程上限
    private String alarmLower;   //报警下限
    private String alarmHigher;   //报警上限
    private String powerOff;   //断电值
    private String powerOn;   //复电值
    private String changeValue;   //变化量
    private String controOff1;   //控制断电一
    private String controFeed1;   //控制馈电一
    private String controOff2;   //控制断电二
    private String controFeed2;   //控制馈电二
    private String controOff3;   //控制断电三
    private String controFeed3;   //控制馈电三
    private String alarmRatio;   //预警系数

    public SafeMonitor() {
    }

    public SafeMonitor(HashMap<String,String> map) {
        setCodeIndex(map.get("codeIndex"));
        setSensorCode(map.get("sensorCode"));
        setSensorRegion(map.get("sensorRegion"));
        setSensorValue(map.get("sensorValue"));
        setName(map.get("name"));
        setSensorType(map.get("sensorType"));
        setUnit(map.get("unit"));
        setRangeLower(map.get("rangeLower"));
        setRangeHigher(map.get("rangeHigher"));
        setAlarmLower(map.get("alarmLower"));
        setAlarmHigher(map.get("alarmHigher"));
        setPowerOff(map.get("powerOff"));
        setPowerOn(map.get("powerOn"));
    }

    public String getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(String codeIndex) {
        this.codeIndex = codeIndex;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorRegion() {
        return sensorRegion;
    }

    public void setSensorRegion(String sensorRegion) {
        this.sensorRegion = sensorRegion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRangeLower() {
        return rangeLower;
    }

    public void setRangeLower(String rangeLower) {
        this.rangeLower = rangeLower;
    }

    public String getRangeHigher() {
        return rangeHigher;
    }

    public void setRangeHigher(String rangeHigher) {
        this.rangeHigher = rangeHigher;
    }

    public String getAlarmLower() {
        return alarmLower;
    }

    public void setAlarmLower(String alarmLower) {
        this.alarmLower = alarmLower;
    }

    public String getAlarmHigher() {
        return alarmHigher;
    }

    public void setAlarmHigher(String alarmHigher) {
        this.alarmHigher = alarmHigher;
    }

    public String getPowerOff() {
        return powerOff;
    }

    public void setPowerOff(String powerOff) {
        this.powerOff = powerOff;
    }

    public String getPowerOn() {
        return powerOn;
    }

    public void setPowerOn(String powerOn) {
        this.powerOn = powerOn;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getControOff1() {
        return controOff1;
    }

    public void setControOff1(String controOff1) {
        this.controOff1 = controOff1;
    }

    public String getControFeed1() {
        return controFeed1;
    }

    public void setControFeed1(String controFeed1) {
        this.controFeed1 = controFeed1;
    }

    public String getControOff2() {
        return controOff2;
    }

    public void setControOff2(String controOff2) {
        this.controOff2 = controOff2;
    }

    public String getControFeed2() {
        return controFeed2;
    }

    public void setControFeed2(String controFeed2) {
        this.controFeed2 = controFeed2;
    }

    public String getControOff3() {
        return controOff3;
    }

    public void setControOff3(String controOff3) {
        this.controOff3 = controOff3;
    }

    public String getControFeed3() {
        return controFeed3;
    }

    public void setControFeed3(String controFeed3) {
        this.controFeed3 = controFeed3;
    }

    public String getAlarmRatio() {
        return alarmRatio;
    }

    public void setAlarmRatio(String alarmRatio) {
        this.alarmRatio = alarmRatio;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }
}
