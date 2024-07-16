package com.example.backend.entities.DTOResp;

import java.util.ArrayList;
import java.util.List;

public class ChartResp {

  private String query;
  private String chart_type;
  private String chartName;
  private Object data;
  private List<String> axisName;

  private String title;
  private String erroMessage;

  private FiltreDTO filtre; // Add this field

  public FiltreDTO getFiltre() {
    return filtre;
  }

  public void setFiltre(FiltreDTO filtre) {
    this.filtre = filtre;
  }

  public ChartResp() {
    axisName = new ArrayList<>();
  }

  public String getChart_type() {
    return chart_type;
  }

  public String getChartName() {
    return chartName;
  }

  public void setChartName(String chartName) {
    this.chartName = chartName;
  }

  public void setChart_type(String chart_type) {
    this.chart_type = chart_type;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public List<String> getAxisName() {
    return axisName;
  }

  public void setAxisName(List<String> axisName) {
    this.axisName = axisName;
  }

  public void addAxisX(String axisX) {
    axisName.add(axisX); // Add axisX to the beginning of axisName list
  }

  public void addAxisY(String axisY) {
    axisName.add(axisY); // Add axisY to the end of axisName list
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getErroMessage() {
    return erroMessage;
  }

  public void setErroMessage(String erroMessage) {
    this.erroMessage = erroMessage;
  }
}
