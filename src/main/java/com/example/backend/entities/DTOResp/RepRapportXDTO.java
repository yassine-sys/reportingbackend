package com.example.backend.entities.DTOResp;

import java.util.List;

public class RepRapportXDTO {
    private String filtre;
    private String field_name;
    private String field_reporting;
    private Long id_field;
    private String operation;
    private String table_rep;
    private String tableref_field_appears;
    private String tableref_field_query;
    private String col1;
    private String col2;
    private String tableJoin;
    private boolean isYcustField;
    private boolean isJoin;
    private boolean isYcustField1;
    private List<RepRapportsYDTO> list_rep_rapport_y;

    public RepRapportXDTO() {
    }

    public String getFiltre() {
        return filtre;
    }

    public void setFiltre(String filtre) {
        this.filtre = filtre;
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

    public String getTable_rep() {
        return table_rep;
    }

    public void setTable_rep(String table_rep) {
        this.table_rep = table_rep;
    }

    public String getTableref_field_appears() {
        return tableref_field_appears;
    }

    public void setTableref_field_appears(String tableref_field_appears) {
        this.tableref_field_appears = tableref_field_appears;
    }

    public String getTableref_field_query() {
        return tableref_field_query;
    }

    public void setTableref_field_query(String tableref_field_query) {
        this.tableref_field_query = tableref_field_query;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getTableJoin() {
        return tableJoin;
    }

    public void setTableJoin(String tableJoin) {
        this.tableJoin = tableJoin;
    }

    public boolean isYcustField() {
        return isYcustField;
    }

    public void setYcustField(boolean ycustField) {
        isYcustField = ycustField;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public void setJoin(boolean join) {
        isJoin = join;
    }

    public boolean isYcustField1() {
        return isYcustField1;
    }

    public void setYcustField1(boolean ycustField1) {
        isYcustField1 = ycustField1;
    }

    public List<RepRapportsYDTO> getList_rep_rapport_y() {
        return list_rep_rapport_y;
    }

    public void setList_rep_rapport_y(List<RepRapportsYDTO> list_rep_rapport_y) {
        this.list_rep_rapport_y = list_rep_rapport_y;
    }
}
