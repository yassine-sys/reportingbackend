package com.example.backend.entities.DTOResp;

import java.math.BigInteger;

public class FileProcessDTO {

    private String filename;
    private String dateReception;
    private Integer statut;
    private BigInteger idRep;
    private BigInteger idRepFils;
    private Integer nbRecord;

    public FileProcessDTO(String filename, String dateReception, Integer statut, BigInteger idRep, BigInteger idRepFils, Integer nbRecord) {
        this.filename = filename;
        this.dateReception = dateReception;
        this.statut = statut;
        this.idRep = idRep;
        this.idRepFils = idRepFils;
        this.nbRecord = nbRecord;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDateReception() {
        return dateReception;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public BigInteger getIdRep() {
        return idRep;
    }

    public void setIdRep(BigInteger idRep) {
        this.idRep = idRep;
    }

    public BigInteger getIdRepFils() {
        return idRepFils;
    }

    public void setIdRepFils(BigInteger idRepFils) {
        this.idRepFils = idRepFils;
    }

    public Integer getNbRecord() {
        return nbRecord;
    }

    public void setNbRecord(Integer nbRecord) {
        this.nbRecord = nbRecord;
    }
}
