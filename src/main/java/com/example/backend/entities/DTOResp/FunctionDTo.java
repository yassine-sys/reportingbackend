package com.example.backend.entities.DTOResp;

import java.util.List;

public class FunctionDTo {
    private Long id;
    private String functionName;

    List<ReportDTO> listeReports;
    private Integer order;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<ReportDTO> getListeReports() {
        return listeReports;
    }

    public void setListeReports(List<ReportDTO> listeReports) {
        this.listeReports = listeReports;
    }
}
