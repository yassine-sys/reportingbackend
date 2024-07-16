package com.example.backend.services;


import com.example.backend.dao.FiltreRepository;
import com.example.backend.dao.RepRapportRepository;
import com.example.backend.dao.RepRapportsXRepository;
import com.example.backend.dao.RepRapportsYRepository;
import com.example.backend.entities.DTOResp.*;
import com.example.backend.entities.EtatCollect;
import com.example.backend.entities.RepRapport;
import com.example.backend.entities.RepRapportsX;
import com.example.backend.entities.RepRapportsY;
import com.example.backend.entities.conf.Filtre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class ReportService {
    private final RepRapportRepository repRapportRepository;
    private final RepRapportsXRepository repRapportsXRepository;

    private final RepRapportsYRepository repRapportsYRepository;
    private FiltreRepository filtreRepository;

    @PersistenceContext
    private EntityManager entityManager;

  @Autowired
  private JdbcTemplate jdbcTemplate;
    @Autowired
    public ReportService(FiltreRepository filtreRepository,RepRapportRepository repRapportRepository,RepRapportsXRepository repRapportsXRepository,RepRapportsYRepository repRapportsYRepository) {
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


    public String generatedetailledQueryFromDTO(RapportDTO rapportDTO) {
        StringBuilder queryBuilder = new StringBuilder();

        // Start building the SELECT clause
        queryBuilder.append("SELECT ");

        // Iterate through rep_rapports_x and add operation as field_name to the SELECT clause
        for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
            queryBuilder.append(repX.getOperation()).append(" AS ").append(repX.getField_name()).append(", ");
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

    @Transactional(rollbackFor = Exception.class)
    public void savedetailledreport(RapportDTO reportDTO) throws Exception {
        try {
            // Convert ReportDTO to RepRapport entity
            RepRapport repRapport = convertToEntity(reportDTO.getRep_rapport());
            // Save RepRapport entity to the database
            RepRapport rep = repRapportRepository.save(repRapport);

            //iterate X
            for(RepRapportXDTO repX : reportDTO.getRep_rapports_x()){
                RepRapportsX repRapportsX = convertToEntityRepX(repX);
                repRapportsX.setRepRapport(repRapport);

              if (isAggregationFunction(repX.getOperation())) {
                // Set Ycustfield to true if it's an aggregation function
                repRapportsX.setYcustfield(true);
              }

              //save X
                RepRapportsX newX = repRapportsXRepository.save(repRapportsX);

              String sql = "INSERT INTO reporting.customisefiltrereport (report_id, myfiltre) VALUES (?, ?)";
              jdbcTemplate.update(sql, repRapportsX.getRepRapport().getId(), repRapportsX.getFiltre());


            }
        } catch (Exception e) {
            throw new Exception("Error occurred while saving report: " + e.getMessage());
        }
    }

    private RepRapport convertToEntity(RepRappotDTO repRapportDTO) {
        RepRapport rep = new RepRapport();
        System.out.println(repRapportDTO.getChart_type());
        rep.setChartType(repRapportDTO.getChart_type());
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
    @Transactional
    public void addData(RapportDTO rapportDTO) {
        RepRapport repRapport = new RepRapport();
        // Set other fields similarly

        repRapportRepository.save(repRapport);

        List<RepRapportXDTO> repRapportXDTOList = rapportDTO.getRep_rapports_x();
        for (RepRapportXDTO repRapportXDTO : repRapportXDTOList) {
            RepRapportsX repRapportX = new RepRapportsX();
            repRapportX.setFiltre(repRapportXDTO.getFiltre());
            // Set other fields similarly
            repRapportX.setRepRapport(repRapport); // Set the relationship

            repRapportsXRepository.save(repRapportX);
        }
    }

    public List<RepRapport> getdetailledreports() {
        String sql = "SELECT name, title, chart_type " +
                "FROM etl.rep_rapports WHERE isdetails =true";

        Query query = entityManager.createNativeQuery(sql);

        return query.getResultList();
    }


    public List<RepRapport> gethasdetailledreports() {
        String sql = "SELECT *" +
                "FROM etl.rep_rapports WHERE hasdetails =true";

        Query query = entityManager.createNativeQuery(sql);

        return query.getResultList();
    }
  public RepRapport initializeLastRepRapport() {
    String sql = "SELECT * FROM etl.rep_rapports ORDER BY id DESC LIMIT 1";
    Query query = entityManager.createNativeQuery(sql, RepRapport.class);
    List<RepRapport> resultList = query.getResultList();
    return resultList.isEmpty() ? null : resultList.get(0);
  }

  public List<RepRapportsX> getlistreprapportx() {
    String sql = "SELECT rx.* FROM etl.rep_rapports_x rx " +
      "WHERE rx.id_rapport = (SELECT MAX(id) FROM etl.rep_rapports)";

    Query query = entityManager.createNativeQuery(sql,RepRapportsX.class);

    return query.getResultList();
  }

  public String generateQuery() {
    StringBuilder queryBuilder = new StringBuilder();
    RepRapport lastRepRapport = initializeLastRepRapport();
    // Get the RepRapportsX list from the last RepRapport
    List<RepRapportsX> repRapportsXList = getlistreprapportx();

    // Construct the SELECT clause
    if (!repRapportsXList.isEmpty()) {
      queryBuilder.append("SELECT ");
      for (RepRapportsX repRapportsX : repRapportsXList) {
        queryBuilder.append(repRapportsX.getOperation())
          .append(" AS ")
          .append(repRapportsX.getFieldName())
          .append(", ");
      }
      queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space

      // Construct the FROM clause
      queryBuilder.append(" FROM tab_temp.").append(repRapportsXList.get(0).getTable_rep());

      // Construct the WHERE clause
      queryBuilder.append(" WHERE ");
      for (RepRapportsX repRapportsX : repRapportsXList) {
        queryBuilder.append(repRapportsX.getFiltre())
          .append(" AND ");
      }
      queryBuilder.delete(queryBuilder.length() - 5, queryBuilder.length()); // Remove the last "AND" and space

      // Construct the GROUP BY clause
      queryBuilder.append(" GROUP BY ");
      for (RepRapportsX repRapportsX : repRapportsXList) {

        if (!isAggregationFunction(repRapportsX.getOperation())){
          queryBuilder.append(repRapportsX.getOperation())
            .append(", ");
        }

      }

      queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space
    } else {
      return "No data found."; // Return a message if there are no elements in the repRapportsXList
    }
      return queryBuilder.toString();
    }

  public List<Map<String, Object>> generateDataFromQuery(String query) {
      return jdbcTemplate.queryForList(query);
  }

  private boolean isAggregationFunction(String operation) {
    // Assuming aggregation functions start with "AVG", "SUM", "COUNT", "MIN", "MAX", etc.
    return operation.startsWith("AVG") || operation.startsWith("avg") || operation.startsWith("SUM") || operation.startsWith("sum") ||
      operation.startsWith("COUNT") || operation.startsWith("count") || operation.startsWith("MIN") || operation.startsWith("min") ||
      operation.startsWith("MAX") || operation.startsWith("max");
  }

  public String generatetestQuery(List<RepRapportXDTO> repRapportsXList) {
    StringBuilder queryBuilder = new StringBuilder();

    // Construct the SELECT clause
    if (!repRapportsXList.isEmpty()) {
      queryBuilder.append("SELECT ");
      for (RepRapportXDTO repRapportsX : repRapportsXList) {
        queryBuilder.append(repRapportsX.getOperation())
          .append(" AS ")
          .append(repRapportsX.getField_name())
          .append(", ");
      }
      queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space

      // Construct the FROM clause
      queryBuilder.append(" FROM tab_temp.").append(repRapportsXList.get(0).getTable_rep());

      // Construct the WHERE clause
      queryBuilder.append(" WHERE ");
      for (RepRapportXDTO repRapportsX : repRapportsXList) {
        queryBuilder.append(repRapportsX.getFiltre())
          .append(" AND ");
      }
      queryBuilder.delete(queryBuilder.length() - 5, queryBuilder.length()); // Remove the last "AND" and space

      // Construct the GROUP BY clause
      queryBuilder.append(" GROUP BY ");
      for (RepRapportXDTO repRapportsX : repRapportsXList) {
        if (!isAggregationFunction(repRapportsX.getOperation())) {
          queryBuilder.append(repRapportsX.getField_name())
            .append(", ");
        }
      }
      queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space
    } else {
      return "No data found."; // Return a message if there are no elements in the repRapportsXList
    }
    return queryBuilder.toString();
  }

  public ChartResp generateChartfromquery(RapportDTO rapportDTO) {
    ChartResp chart = new ChartResp();

    // Generate the query using the existing generatetestQuery method
    String query = generatetestQuery(rapportDTO.getRep_rapports_x());
    chart.setQuery(query);

    // Set the chart type and name
    chart.setChart_type(rapportDTO.getRep_rapport().getChart_type());
    chart.setChartName(rapportDTO.getRep_rapport().getTitle());
    chart.setTitle(rapportDTO.getRep_rapport().getTitle());

    // Populate the axis names
    List<String> names = new ArrayList<>();
    for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
      if (!names.contains(repX.getField_name())) {
        names.add(repX.getField_name());
        chart.addAxisX(repX.getField_reporting());
      }
   /*   for (RepRapportsYDTO repY : repX.getList_rep_rapport_y()) {
        chart.addAxisY(repY.getField_reporting());
      } */
    }

    try {
      // Execute the query using EntityManager
      Object queryResult = executeQuery(chart.getQuery());
      chart.setData(queryResult);
    } catch (Exception e) {
      chart.setErroMessage(e.getMessage());
    }

    return chart;
  }

  private Object executeQuery(String query) {
    // Execute the query using EntityManager
    return entityManager.createNativeQuery(query).getResultList();
  }


    public String generateQueryFromDTOCustomize(RapportDTO reportDTO) {
        StringBuilder queryBuilder = new StringBuilder();

        boolean hasJoin = reportDTO.getRep_rapports_x().stream().anyMatch(RepRapportXDTO::isJoin);

        queryBuilder.append("SELECT ");

        // Map to store prefixes for each field name
        Map<String, String> prefixMap = new HashMap<>();

        // Set to store selected columns
        Set<String> selectedColumns = new HashSet<>();

        // Construct the SELECT clause
        boolean isFirstSelectItem = true;
        for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
            if (repX.getCol2() != null && !repX.getCol2().isEmpty() && !selectedColumns.contains(repX.getCol2())) {
                if (!isFirstSelectItem) {
                    queryBuilder.append(", ");
                }
                String prefix = getTablePrefix(repX.getField_name(), prefixMap);
                queryBuilder.append(prefix).append(".").append(repX.getCol2()).append(" AS ").append(repX.getField_name());
                isFirstSelectItem = false;
                selectedColumns.add(repX.getCol2());
            } else if (repX.getOperation() != null && !repX.getOperation().isEmpty() && !selectedColumns.contains(repX.getOperation())) {
                if (!isFirstSelectItem) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(repX.getOperation()).append(" AS ").append(repX.getField_name());
                isFirstSelectItem = false;
                selectedColumns.add(repX.getOperation());
            }
        }

        // Construct the FROM clause
        queryBuilder.append(" FROM ");
        boolean isFirstTable = true;
        for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
            if (repX.getTableJoin() != null && !repX.getTableJoin().isEmpty()) {
                if (!isFirstTable) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append("tableref.").append(repX.getTableJoin()).append(" AS ").append(getTablePrefix(repX.getField_name(), prefixMap));
                isFirstTable = false;
            }
        }

        // Add the comma before "stat" if there are additional tables joined from tableref
        if (!isFirstTable) {
            queryBuilder.append(", ");
        }

        queryBuilder.append("stat.").append(reportDTO.getRep_rapports_x().get(0).getTable_rep()).append(" AS T3");

        // Construct the WHERE clause for join
        if (hasJoin) {
            queryBuilder.append(" WHERE ");
            boolean isFirstCondition = true;
            for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
                if (repX.isJoin() && repX.getCol1() != null && repX.getCol2() != null) {
                    if (!isFirstCondition) {
                        queryBuilder.append(" AND ");
                    }
                    String prefix1 = getTablePrefix(repX.getField_name(), prefixMap);
                    String prefix2 = getTablePrefix(repX.getTableJoin(), prefixMap);
                    queryBuilder.append(prefix1).append(".").append(repX.getCol1()).append(" = T3.").append(repX.getField_name());
                    isFirstCondition = false;
                }
            }
        }

        String filtreValue = reportDTO.getFiltre().getMyfiltre();
        if (filtreValue != null && !filtreValue.isEmpty()) {
            if (hasJoin) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            queryBuilder.append(filtreValue);
        }
        queryBuilder.append(" GROUP BY ");
        boolean isFirstGroupByItem = true;
        for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
            if (repX.getCol2() != null && !repX.getCol2().isEmpty()) {
                if (!isFirstGroupByItem) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(getTablePrefix(repX.getField_name(), prefixMap)).append(".").append(repX.getCol2());
                isFirstGroupByItem = false;
            }
        }

        // Add non-ycustField operations to the GROUP BY clause
        for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
            if (!repX.isYcustField() && repX.getOperation() != null && !repX.getOperation().isEmpty()) {
                if (!isFirstGroupByItem) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(repX.getOperation());
                isFirstGroupByItem = false;
            }
        }

        return queryBuilder.toString();
    }


    private String getTablePrefix(String fieldName, Map<String, String> prefixMap) {
        // Logic to determine the prefix based on the field name
        // You can modify this logic as per your requirements
        if (!prefixMap.containsKey(fieldName)) {
            prefixMap.put(fieldName, prefixMap.size() % 2 == 0 ? "T1" : "T2");
        }
        return prefixMap.get(fieldName);
    }

    public ChartResp generateChartDataC(RapportDTO rapportDTO) {
        ChartResp chart = new ChartResp();
        chart.setQuery(generateQueryFromDTOCustomize(rapportDTO)); // Generate query based on DTO
        chart.setChart_type(rapportDTO.getRep_rapport().getChart_type());
        chart.setChartName(rapportDTO.getRep_rapport().getTitle());
        FiltreDTO filtre = new FiltreDTO();
        filtre.setMyfiltre(rapportDTO.getFiltre().getMyfiltre());
        chart.setFiltre(filtre);

        List<String> names = new ArrayList<>();

        for (RepRapportXDTO repX : rapportDTO.getRep_rapports_x()) {
            if (!names.contains(repX.getField_name())) {
                names.add(repX.getField_name());
                chart.addAxisX(repX.getField_reporting());
            }
        }

        try {
            // Execute the query using EntityManager or your preferred method
            Object queryResult = executeQuery(chart.getQuery()); // Execute the generated query
            chart.setData(queryResult); // Set the fetched data to the chart response
        } catch (Exception e) {
            chart.setErroMessage(e.getMessage()); // Set error message if an exception occurs
        }
        return chart;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveReportCustomize(RapportDTO reportDTO) throws Exception {
        try {
            // Convert ReportDTO to RepRapport entity
            RepRapport repRapport = convertToEntity(reportDTO.getRep_rapport());
            // Save RepRapport entity to the database
            RepRapport rep = repRapportRepository.save(repRapport);

            // Save filtre only if it has not been saved for this idreport before
            if (filtreRepository.findByFiltreAndRepRapportId( reportDTO.getFiltre().getMyfiltre(), repRapport.getId()) == null) {
                // Save filtre to customisefiltrereport table
                Filtre filtre = new Filtre();
                filtre.setFiltre(reportDTO.getFiltre().getMyfiltre());
                filtre.setRepRapport(repRapport);
                filtreRepository.save(filtre); // Assuming filtreRepository is your repository for Filtre entity
            }

            // Iterate over RepRapportXDTO list and save each RepRapportsX entity
            for (RepRapportXDTO repX : reportDTO.getRep_rapports_x()) {
                RepRapportsX repRapportsX = convertToEntityRepX(repX);
                repRapportsX.setRepRapport(repRapport);
                // Save RepRapportsX entity to the database
                RepRapportsX newX = repRapportsXRepository.save(repRapportsX);
            }

        } catch (Exception e) {
            throw new Exception("Error occurred while saving report: " + e.getMessage());
        }
    }


}
