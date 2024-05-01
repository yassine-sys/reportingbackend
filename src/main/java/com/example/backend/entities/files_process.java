package com.example.backend.entities;

import javax.persistence.Entity;
import java.math.BigInteger;

public class files_process {

    private String filename;

    private String date_reception;
    private Integer statut;

    private BigInteger id_rep;

    private BigInteger id_rep_fils;

    private Integer nb_record;

    public files_process(String filename, String date_reception, Integer statut, BigInteger id_rep, BigInteger id_rep_fils, Integer nb_record) {
        this.filename = filename;
        this.date_reception = date_reception;
        this.statut = statut;
        this.id_rep = id_rep;
        this.id_rep_fils = id_rep_fils;
        this.nb_record = nb_record;
    }

    public files_process() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDate_reception() {
        return date_reception;
    }

    public void setDate_reception(String date_reception) {
        this.date_reception = date_reception;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public BigInteger getId_rep() {
        return id_rep;
    }

    public void setId_rep(BigInteger id_rep) {
        this.id_rep = id_rep;
    }

    public BigInteger getId_rep_fils() {
        return id_rep_fils;
    }

    public void setId_rep_fils(BigInteger id_rep_fils) {
        this.id_rep_fils = id_rep_fils;
    }

    public Integer getNb_record() {
        return nb_record;
    }

    public void setNb_record(Integer nb_record) {
        this.nb_record = nb_record;
    }
}
