package com.example.backend.entities.conf;

import com.example.backend.entities.RepRapport;

import javax.persistence.*;

@Entity
@Table(name = "customisefiltrereport", schema = "reporting")
public class Filtre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "myfiltre") // Map the filtre field to the database column
    private String filtre;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private RepRapport repRapport;


    public Filtre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiltre() {
        return filtre;
    }

    public void setFiltre(String filtre) {
        this.filtre = filtre;
    }

    public RepRapport getRepRapport() {
        return repRapport;
    }

    public void setRepRapport(RepRapport repRapport) {
        this.repRapport = repRapport;
    }


}
