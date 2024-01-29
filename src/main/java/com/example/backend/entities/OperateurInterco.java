package com.example.backend.entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "operateur_interco")
@Table(schema = "tableref")
public class OperateurInterco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String operateur;
    private String code_operateurs;
    private Long id_pays;
    private String type;
    private Long id_monnaie;
    private String nom_utilisateur;
    private Date date_modif;
    private String address;
    private String contact;
    private String tel_contact;

    public OperateurInterco() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public String getCode_operateurs() {
        return code_operateurs;
    }

    public void setCode_operateurs(String code_operateurs) {
        this.code_operateurs = code_operateurs;
    }

    public Long getId_pays() {
        return id_pays;
    }

    public void setId_pays(Long id_pays) {
        this.id_pays = id_pays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId_monnaie() {
        return id_monnaie;
    }

    public void setId_monnaie(Long id_monnaie) {
        this.id_monnaie = id_monnaie;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public Date getDate_modif() {
        return date_modif;
    }

    public void setDate_modif(Date date_modif) {
        this.date_modif = date_modif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTel_contact() {
        return tel_contact;
    }

    public void setTel_contact(String tel_contact) {
        this.tel_contact = tel_contact;
    }
}
