package com.example.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Entity(name = "missing_file")
@Table(schema = "stat")
public class MissingFiles implements Serializable {
    @Id
    private String filename;
    private Long id_rep;
    private Long id_flow;

    public MissingFiles() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
}
