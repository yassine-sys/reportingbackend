package com.example.backend.entities;


import javax.persistence.Entity;
import javax.persistence.Table;


@Table(schema = "tableref")
public class commentlcr {
    private String lcroperator;
    private String destinationoperator;
    private String comment;

    public String getLcroperator() {
        return lcroperator;
    }

    public void setLcroperator(String lcroperator) {
        this.lcroperator = lcroperator;
    }

    public String getDestinationoperator() {
        return destinationoperator;
    }

    public void setDestinationoperator(String destinationoperator) {
        this.destinationoperator = destinationoperator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
