package com.example.backend.entities;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MapInfoDTO {
    private String site_id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double  duration;
    private Integer call_count;
    private Integer subscribers;

    private String technology;


    public MapInfoDTO(String site_id, BigDecimal latitude, BigDecimal longitude, Double  duration, Integer call_count, Integer subscribers,String technology) {
        this.site_id = site_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.duration = duration;
        this.call_count = call_count;
        this.subscribers = subscribers;
        this.technology = technology;
    }

    public MapInfoDTO() {
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Double  getDuration() {
        return duration;
    }

    public void setDuration(Double  duration) {
        this.duration = duration;
    }

    public Integer getCall_count() {
        return call_count;
    }

    public void setCall_count(Integer call_count) {
        this.call_count = call_count;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
