package com.example.backend.entities;

import java.util.List;

public class MapRequest {
    private String startDate;
    private String endDate;
    private List<String> typeCalls;
    private String order;
    private Integer limit;
    private  String typeCdr;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getTypeCalls() {
        return typeCalls;
    }

    public void setTypeCalls(List<String> typeCalls) {
        this.typeCalls = typeCalls;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getTypeCdr() {
        return typeCdr;
    }

    public void setTypeCdr(String typeCdr) {
        this.typeCdr = typeCdr;
    }
}
