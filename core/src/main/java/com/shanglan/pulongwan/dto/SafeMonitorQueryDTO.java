package com.shanglan.pulongwan.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SafeMonitorQueryDTO {

    private String keyword;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate queryDate;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    public LocalDate getQueryDate() {
//        return queryDate;
//    }
//
//    public void setQueryDate(LocalDate queryDate) {
//        this.queryDate = queryDate;
//    }
}
