package com.example.backend.entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "taux_change")
@Table(schema = "tableref")
public class ChangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long id_monnaie;
    private Float taux_change;
    private Date date_debut_validite;
    private Date date_modif;
    private String nom_utilisateur;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getId_monnaie() {
        return id_monnaie;
    }

    public void setId_monnaie(Long id_monnaie) {
        this.id_monnaie = id_monnaie;
    }

    public Float getTaux_change() {
        return taux_change;
    }

    public void setTaux_change(Float taux_change) {
        this.taux_change = taux_change;
    }

    public Date getDate_debut_validite() {
        return date_debut_validite;
    }

    public void setDate_debut_validite(Date date_debut_validite) {
        this.date_debut_validite = date_debut_validite;
    }

    public Date getDate_modif() {
        return date_modif;
    }

    public void setDate_modif(Date date_modif) {
        this.date_modif = date_modif;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }
}
