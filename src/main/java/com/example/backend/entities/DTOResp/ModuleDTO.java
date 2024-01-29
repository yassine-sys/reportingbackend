package com.example.backend.entities.DTOResp;

import com.example.backend.entities.SubModule;

import java.util.List;

public class ModuleDTO {
    private Long id;
    private String moduleName;
    private Integer order;

    private List<SubModuleDTO> listSubModule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<SubModuleDTO> getListSubModule() {
        return listSubModule;
    }

    public void setListSubModule(List<SubModuleDTO> listSubModule) {
        this.listSubModule = listSubModule;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
