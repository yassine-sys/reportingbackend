package com.example.backend.entities.DTOResp;

import javax.persistence.Column;

public class ReportDTO {
    private Long id;
    private String name;

    private String title;


    public ReportDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
