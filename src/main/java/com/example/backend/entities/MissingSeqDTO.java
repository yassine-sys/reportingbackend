package com.example.backend.entities;

import javax.persistence.Id;

public class MissingSeqDTO {
    private Long id_rep;
    private String flowName;
    private String date_detection;
    private Long missedseq;
    private String nextrcvdfile;
    private String rep_name;
    private String node;

    public MissingSeqDTO() {
    }

    public Long getId_rep() {
        return id_rep;
    }

    public void setId_rep(Long id_rep) {
        this.id_rep = id_rep;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getDate_detection() {
        return date_detection;
    }

    public void setDate_detection(String date_detection) {
        this.date_detection = date_detection;
    }

    public Long getMissedseq() {
        return missedseq;
    }

    public void setMissedseq(Long missedseq) {
        this.missedseq = missedseq;
    }

    public String getNextrcvdfile() {
        return nextrcvdfile;
    }

    public void setNextrcvdfile(String nextrcvdfile) {
        this.nextrcvdfile = nextrcvdfile;
    }

    public String getRep_name() {
        return rep_name;
    }

    public void setRep_name(String rep_name) {
        this.rep_name = rep_name;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
