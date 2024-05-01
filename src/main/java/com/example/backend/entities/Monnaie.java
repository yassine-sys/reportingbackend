package com.example.backend.entities;

import javax.persistence.*;

@Entity(name = "monnaie")
@Table(schema = "tableref")
public class Monnaie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code_monnai;

    private String monnai;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCode_monnai() {
        return code_monnai;
    }

    public void setCode_monnai(String code_monnai) {
        this.code_monnai = code_monnai;
    }

    public String getMonnai() {
        return monnai;
    }

    public void setMonnai(String monnai) {
        this.monnai = monnai;
    }
}
