package com.example.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

public class EtatCollect {
    private String date;

    private String name_flow;
    private Integer nbfiles;
    private Integer nbrecord;
    private Integer nbfileprocessed;
    private Integer nbfilecorrupted;
    private Integer average;
//    private String frequency ;
    private  String name_rep;

    public EtatCollect() {
    }

    public EtatCollect(String date, String name_flow, Integer nbfiles, Integer nbrecord, Integer nbfileprocessed, Integer nbfilecorrupted, Integer average,String frequency,String name_rep) {
        this.date = date;
        this.name_flow = name_flow;
        this.nbfiles = nbfiles;
        this.nbrecord = nbrecord;
        this.nbfileprocessed = nbfileprocessed;
        this.nbfilecorrupted = nbfilecorrupted;
        this.average = average;
//        this.frequency = frequency;
        this.name_rep = name_rep;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName_flow() {
        return name_flow;
    }

    public void setName_flow(String name_flow) {
        this.name_flow = name_flow;
    }

    public Integer getNbfiles() {
        return nbfiles;
    }

    public void setNbfiles(Integer nbfiles) {
        this.nbfiles = nbfiles;
    }

    public Integer getNbrecord() {
        return nbrecord;
    }

    public void setNbrecord(Integer nbrecord) {
        this.nbrecord = nbrecord;
    }

    public Integer getNbfileprocessed() {
        return nbfileprocessed;
    }

    public void setNbfileprocessed(Integer nbfileprocessed) {
        this.nbfileprocessed = nbfileprocessed;
    }

    public Integer getNbfilecorrupted() {
        return nbfilecorrupted;
    }

    public void setNbfilecorrupted(Integer nbfilecorrupted) {
        this.nbfilecorrupted = nbfilecorrupted;
    }

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    public String getName_rep() {
        return name_rep;
    }

    public void setName_rep(String name_rep) {
        this.name_rep = name_rep;
    }
    //    public String getFrequency() {
//        return frequency;
//    }
//
//    public void setFrequency(String frequency) {
//        this.frequency = frequency;
//    }
}
