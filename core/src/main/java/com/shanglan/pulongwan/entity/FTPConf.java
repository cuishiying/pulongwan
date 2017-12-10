package com.shanglan.pulongwan.entity;

import com.shanglan.pulongwan.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by cuishiying on 2017/5/10.
 * 一级主题
 */
@Entity
@Table(name = "cnoa_ftp_conf")
public class FTPConf extends BaseEntity{


    private static final long serialVersionUID = 2435596822381639885L;

    @Column(unique = true)
    private String name;
    private String ftppath;
    private String monitorfile;
    private boolean monitoring;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFtppath() {
        return ftppath;
    }

    public void setFtppath(String ftppath) {
        this.ftppath = ftppath;
    }

    public String getMonitorfile() {
        return monitorfile;
    }

    public void setMonitorfile(String monitorfile) {
        this.monitorfile = monitorfile;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }
}
