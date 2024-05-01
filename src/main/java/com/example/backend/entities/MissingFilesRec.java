package com.example.backend.entities;

import java.math.BigDecimal;

public class MissingFilesRec {
    private String date_ixtools ;
    private String cdrfilename;
    private Integer cdr_countix;
    private BigDecimal dur;
    private String date_switch;
    private String filename;
    private BigDecimal dur_swt;
    private Integer cdr_countswt;

    public MissingFilesRec() {
    }

    public String getDate_ixtools() {
        return date_ixtools;
    }

    public void setDate_ixtools(String date_ixtools) {
        this.date_ixtools = date_ixtools;
    }

    public String getCdrfilename() {
        return cdrfilename;
    }

    public void setCdrfilename(String cdrfilename) {
        this.cdrfilename = cdrfilename;
    }

    public Integer getCdr_countix() {
        return cdr_countix;
    }

    public void setCdr_countix(Integer cdr_countix) {
        this.cdr_countix = cdr_countix;
    }

    public BigDecimal getDur() {
        return dur;
    }

    public void setDur(BigDecimal dur) {
        this.dur = dur;
    }

    public String getDate_switch() {
        return date_switch;
    }

    public void setDate_switch(String date_switch) {
        this.date_switch = date_switch;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public BigDecimal getDur_swt() {
        return dur_swt;
    }

    public void setDur_swt(BigDecimal dur_swt) {
        this.dur_swt = dur_swt;
    }

    public Integer getCdr_countswt() {
        return cdr_countswt;
    }

    public void setCdr_countswt(Integer cdr_countswt) {
        this.cdr_countswt = cdr_countswt;
    }
}
