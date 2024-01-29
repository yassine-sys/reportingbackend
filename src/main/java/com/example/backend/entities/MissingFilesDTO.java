package com.example.backend.entities;

public class MissingFilesDTO {
    private String filename;
    private String name_rep;
    private String flow;

    public MissingFilesDTO() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName_rep() {
        return name_rep;
    }

    public void setName_rep(String name_rep) {
        this.name_rep = name_rep;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
