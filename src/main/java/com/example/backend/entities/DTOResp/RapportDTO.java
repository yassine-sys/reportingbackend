package com.example.backend.entities.DTOResp;

import com.example.backend.entities.RepRapport;
import com.example.backend.entities.RepRapportsX;

import java.util.List;

public class RapportDTO {
    private String report_type;
    private List<Object> flow;
    private RepRappotDTO rep_rapport;
    private List<RepRapportXDTO> rep_rapports_x;

    private FiltreDTO filtre;

    public RapportDTO() {
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public List<Object> getFlow() {
        return flow;
    }

    public void setFlow(List<Object> flow) {
        this.flow = flow;
    }

    public RepRappotDTO getRep_rapport() {
        return rep_rapport;
    }

    public void setRep_rapport(RepRappotDTO rep_rapport) {
        this.rep_rapport = rep_rapport;
    }

    public List<RepRapportXDTO> getRep_rapports_x() {
        return rep_rapports_x;
    }

    public void setRep_rapports_x(List<RepRapportXDTO> rep_rapports_x) {
        this.rep_rapports_x = rep_rapports_x;
    }

    public FiltreDTO getFiltre() {
        return filtre;
    }

    public void setFiltre(FiltreDTO filtre) {
        this.filtre = filtre;
    }
}
