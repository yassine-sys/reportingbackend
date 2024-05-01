package com.example.backend.services;


import com.example.backend.dao.RepRapportRepository;
import com.example.backend.dao.RepRapportsXRepository;
import com.example.backend.dao.RepRapportsYRepository;
import com.example.backend.entities.DTOResp.*;
import com.example.backend.entities.RepRapport;
import com.example.backend.entities.RepRapportsX;
import com.example.backend.entities.RepRapportsY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final RepRapportRepository repRapportRepository;
    private final RepRapportsXRepository repRapportsXRepository;

    private final RepRapportsYRepository repRapportsYRepository;

    @Autowired
    public ReportService(RepRapportRepository repRapportRepository,RepRapportsXRepository repRapportsXRepository,RepRapportsYRepository repRapportsYRepository) {
        this.repRapportRepository = repRapportRepository;
        this.repRapportsXRepository = repRapportsXRepository;
        this.repRapportsYRepository = repRapportsYRepository;
    }

    public String generateQueryFromDTO(RapportDTO rapportDTO) {
        StringBuilder queryBuilder = new StringBuilder();

        // Start building the SELECT clause
        queryBuilder.append("SELECT ");

        // Iterate through rep_rapports_x and add operation as field_name to the SELECT clause
        for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
            queryBuilder.append(repX.getOperation()).append(" AS ").append(repX.getField_name()).append(", ");
        }

        // Add list_rep_rapport_y to the SELECT clause
        for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
            for (RepRapportsYDTO repY : repX.getList_rep_rapport_y()) {
                queryBuilder.append(repY.getOperation()).append(" AS ").append(repY.getField_name()).append(", ");
            }
        }

        // Remove the trailing comma and space
        if (queryBuilder.length() > 0) {
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        }

        // Add the FROM clause using the first rep_rapports_x entry
        if (!rapportDTO.getRep_rapports_x().isEmpty()) {
            RepRapportXDTO firstRepX = rapportDTO.getRep_rapports_x().get(0);
            queryBuilder.append(" FROM ").append(firstRepX.getTable_rep());
        }

        // Add the GROUP BY clause using rep_rapports_x.field_name
        queryBuilder.append(" GROUP BY ");
        for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
            queryBuilder.append(repX.getField_name()).append(", ");
        }
        // Remove the trailing comma and space
        if (queryBuilder.length() > 0) {
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        }

        // Return the generated query
        return queryBuilder.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveReport(RapportDTO reportDTO) throws Exception {
        try {
            // Convert ReportDTO to RepRapport entity
            RepRapport repRapport = convertToEntity(reportDTO.getRep_rapport());
            // Save RepRapport entity to the database
            RepRapport rep = repRapportRepository.save(repRapport);

            //iterate X
            for(RepRapportXDTO repX : reportDTO.getRep_rapports_x()){
                RepRapportsX repRapportsX = convertToEntityRepX(repX);
                repRapportsX.setRepRapport(repRapport);
                //save X
                RepRapportsX newX = repRapportsXRepository.save(repRapportsX);

                //iterate Y
                for (RepRapportsYDTO repY : repX.getList_rep_rapport_y()){
                    RepRapportsY repRapportsY = convertToRepRapportsYList(repY);
                    repRapportsY.setRepRapportsX(newX);
                    //save y
                    repRapportsYRepository.save(repRapportsY);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error occurred while saving report: " + e.getMessage());
        }
    }


    private RepRapport convertToEntity(RepRappotDTO repRapportDTO) {
        RepRapport rep = new RepRapport();
        System.out.println(repRapportDTO.getChartType());
        rep.setChartType(repRapportDTO.getChartType());
        rep.setName(repRapportDTO.getName());
        rep.setTitle(repRapportDTO.getTitle());
        rep.setFieldRepport_merge(repRapportDTO.getFieldrepport_merge());
        rep.setOperetionFieldMerge(repRapportDTO.getOperetionfieldmerge());
        rep.setFieldMerge(repRapportDTO.isFieldMerge());
        rep.setCol1(repRapportDTO.getCol1());
        rep.setCol2(repRapportDTO.getCol2());
        rep.setTable_join(repRapportDTO.getTable_join());
        rep.setJoinTable(repRapportDTO.isJoinTable());
        rep.setIscustomise(repRapportDTO.isCustomize());
        rep.setIsDetails(repRapportDTO.isDetails());
        rep.setType_flow(repRapportDTO.getType_flow());
        rep.setIspercent(repRapportDTO.isPercent());
        rep.setLimitnumber(repRapportDTO.getLimitNumber());
        rep.setPercent(repRapportDTO.isPercent());
        rep.setIslimited(repRapportDTO.isLimited());
        rep.setIssimplepie(repRapportDTO.isSimplePie());
        rep.setIsoperator(repRapportDTO.isOperator());
        rep.setLoad(repRapportDTO.isLoad());
        rep.setIscaracter(repRapportDTO.isCarrier());
        rep.setIsnested(repRapportDTO.isNested());
        rep.setIscaracter(repRapportDTO.isCharacter());
        rep.setIsdiff(repRapportDTO.isDiff());
        rep.setHasdetails(repRapportDTO.isHasDetails());
        rep.setHasdate(repRapportDTO.isHasDate());
        return rep;
    }

    private RepRapportsX convertToEntityRepX(RepRapportXDTO repRapportDTO){
        RepRapportsX repRapportsX = new RepRapportsX();
        repRapportsX.setFiltre(repRapportDTO.getFiltre());
        repRapportsX.setFieldName(repRapportDTO.getField_name());
        repRapportsX.setFieldReporting(repRapportDTO.getField_reporting());
        repRapportsX.setIdField(repRapportDTO.getId_field());
        repRapportsX.setOperation(repRapportDTO.getOperation());
        repRapportsX.setTable_rep(repRapportDTO.getTable_rep());
        repRapportsX.setTableref_field_query(repRapportDTO.getTableref_field_query());
        repRapportsX.setTableref_field_appears(repRapportDTO.getTableref_field_appears());
        repRapportsX.setCol1(repRapportDTO.getCol1());
        repRapportsX.setCol2(repRapportDTO.getCol2());
        repRapportsX.setTable_join(repRapportDTO.getTableJoin());
        repRapportsX.setYcustfield(repRapportDTO.isYcustField());
        repRapportsX.setIs_join(repRapportDTO.isJoin());
        repRapportsX.setYcustfield(repRapportDTO.isYcustField1());

        // Assuming you have a method to convert RepRapportsYDTO to RepRapportsY
//        List<RepRapportsY> repRapportsYList = convertToRepRapportsYList(repRapportDTO.getList_rep_rapport_y());
//        repRapportsX.setRepRapportsies(repRapportsYList);

        return repRapportsX;
    }
    private RepRapportsY convertToRepRapportsYList(RepRapportsYDTO repRapportsYDTO) {
            RepRapportsY repRapportsY = new RepRapportsY();
            repRapportsY.setFieldName(repRapportsYDTO.getField_name());
            repRapportsY.setFieldReporting(repRapportsYDTO.getField_reporting());
            repRapportsY.setIdField(repRapportsYDTO.getId_field());
            repRapportsY.setOperation(repRapportsYDTO.getOperation());
        return repRapportsY;
    }

}
