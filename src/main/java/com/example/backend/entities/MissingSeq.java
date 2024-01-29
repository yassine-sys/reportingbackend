package com.example.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "missing_sequences")
@Table(schema = "stat")
public class MissingSeq implements Serializable {
    private Long id_rep;
    private Long id_flow;
    private String date_detection;
    @Id
    private Long missedseq;
    private String nextrcvdfile;
    private String rep_name;
    private String node;

    public MissingSeq() {
    }

    public Long getId_rep() {
        return id_rep;
    }

    public void setId_rep(Long id_rep) {
        this.id_rep = id_rep;
    }

    public Long getId_flow() {
        return id_flow;
    }

    public void setId_flow(Long id_flow) {
        this.id_flow = id_flow;
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
