package com.example.backend.entities.DTOResp;

public class RepRapportsYDTO {
    private String field_name;
    private String field_reporting;
    private Long id_field;
    private String operation;

    public RepRapportsYDTO() {
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_reporting() {
        return field_reporting;
    }

    public void setField_reporting(String field_reporting) {
        this.field_reporting = field_reporting;
    }

    public Long getId_field() {
        return id_field;
    }

    public void setId_field(Long id_field) {
        this.id_field = id_field;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
