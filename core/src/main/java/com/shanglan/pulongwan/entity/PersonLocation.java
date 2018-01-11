package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;

/**
 * 人员定位
 */
@Entity
@Table(name = "cnoa_person_location")
public class PersonLocation extends BaseEntity{
    private static final long serialVersionUID = 6435330055167901828L;

    private String codeIndex;
    private String name;
    private String duty;
    private String groupTeam;
    private String addr1;
    private String addr2;
    private String addr3;
    private String inboxTime;
    private String ynUnderground;
    private String undergroundTime;

    public PersonLocation() {
    }

    public PersonLocation(HashMap<String,String> map) {
        setCodeIndex(map.get("codeIndex"));
        setName(map.get("name"));
        setDuty(map.get("duty"));
        setGroupTeam(map.get("groupTeam"));
        setAddr1(map.get("addr1"));
        setAddr2(map.get("addr2"));
        setAddr3(map.get("addr3"));
        setInboxTime(map.get("inboxTime"));
        setYnUnderground(map.get("ynUnderground"));
        setUndergroundTime(map.get("undergroundTime"));
    }

    public String getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(String codeIndex) {
        this.codeIndex = codeIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getGroupTeam() {
        return groupTeam;
    }

    public void setGroupTeam(String groupTeam) {
        this.groupTeam = groupTeam;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    public String getInboxTime() {
        return inboxTime;
    }

    public void setInboxTime(String inboxTime) {
        this.inboxTime = inboxTime;
    }

    public String getYnUnderground() {
        return ynUnderground;
    }

    public void setYnUnderground(String ynUnderground) {
        this.ynUnderground = ynUnderground;
    }

    public String getUndergroundTime() {
        return undergroundTime;
    }

    public void setUndergroundTime(String undergroundTime) {
        this.undergroundTime = undergroundTime;
    }
}
