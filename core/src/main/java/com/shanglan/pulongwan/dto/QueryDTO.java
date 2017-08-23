package com.shanglan.pulongwan.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class QueryDTO {
    private String topic;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate queryDate;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDate getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(LocalDate queryDate) {
        this.queryDate = queryDate;
    }
}
