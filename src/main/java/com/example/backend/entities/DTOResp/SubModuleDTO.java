package com.example.backend.entities.DTOResp;

import java.util.List;

public class SubModuleDTO {
    private Long id;
    private String subModuleName;
    private Integer order;
    private List<FunctionDTo> liste_functions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubModuleName() {
        return subModuleName;
    }

    public void setSubModuleName(String subModuleName) {
        this.subModuleName = subModuleName;
    }

    public List<FunctionDTo> getListe_functions() {
        return liste_functions;
    }

    public void setListe_functions(List<FunctionDTo> liste_functions) {
        this.liste_functions = liste_functions;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
