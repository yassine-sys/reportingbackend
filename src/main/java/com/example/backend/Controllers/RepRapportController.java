package com.example.backend.Controllers;

import com.example.backend.dao.RepRapportRepository;
import com.example.backend.entities.DTOResp.ChartResp;
import com.example.backend.entities.DTOResp.RapportDTO;
import com.example.backend.entities.DTOResp.RepRapportXDTO;
import com.example.backend.entities.RepRapport;
import com.example.backend.entities.RepRapportsX;
import com.example.backend.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/rapport")
public class RepRapportController {
    @Autowired
    RepRapportRepository repRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final ReportService reportService;

    @Autowired
    public RepRapportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/list")
    public List<RepRapport> getList() {
        return repRepo.findAll();
    }

    @RequestMapping(value="/add", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveReport(@RequestBody RapportDTO reportDTO) {
        try {
            reportService.saveReport(reportDTO);
            return ResponseEntity.ok("Report saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save report: " + e.getMessage());
        }
    }


    @RequestMapping(value="/savedetailledreport", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savedetailledreport(@RequestBody RapportDTO reportDTO) {
        try {
            reportService.savedetailledreport(reportDTO);
            return ResponseEntity.ok("Report saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save report: " + e.getMessage());
        }
    }


    @RequestMapping(value="/query", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getQuery(@RequestBody RapportDTO reportDTO) {
        try {
            String query = reportService.generateQueryFromDTO(reportDTO);
            return ResponseEntity.ok(query);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save report: " + e.getMessage());
        }
    }


    @RequestMapping(value="/detailledquery", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getdetailledquery(@RequestBody RapportDTO reportDTO) {
        try {
            String query = reportService.generatedetailledQueryFromDTO(reportDTO);
            return ResponseEntity.ok(query);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save report: " + e.getMessage());
        }
    }


    @PostMapping("/addreport")
    public ResponseEntity<String> addRapport(@RequestBody RapportDTO rapportDTO) {
        try {
            reportService.addData(rapportDTO);
            return new ResponseEntity<>("Rapport added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add rapport: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/listdetailled")
    public List<RepRapport> listdetailled() {
        return reportService.getdetailledreports();
    }

    @RequestMapping(value = "/gethasdetailledreports")
    public List<RepRapport> gethasdetailledreports() {
        return reportService.gethasdetailledreports();
    }

  /*  @PostMapping("/assignReport")
    public ResponseEntity<String> assignReport(@RequestParam("parentId") Long parentId,
                                               @RequestParam("subReportId") Long subReportId) {
        try {
            // Execute SQL to insert the assignment into the reportssubreports table
            String sql = "INSERT INTO reportssubreports (idreportpere, idsubrep, level, sommet, wherefield, wherefieldreppere) VALUES (?, ?, ?, false, ?, ?)";
            jdbcTemplate.update(sql, parentId, subReportId);

            return ResponseEntity.ok("Report assigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign report: " + e.getMessage());
        }
    }*/


  @PostMapping("/assignReport")
  public ResponseEntity<String> assignReport(@RequestBody Map<String, Object> params) {
    Integer parentId = (Integer) params.get("parentId");
    Integer subReportId = (Integer) params.get("subReportId");
    Integer level = (Integer) params.get("level");
    String wherefield = (String) params.get("wherefield");
    String wherefieldreppere = (String) params.get("wherefieldreppere");
    try {
      // Execute SQL to insert the assignment into the reportsubreports table
      String sql = "INSERT INTO reporting.reportsubreports (idreportpere, idsubrep, level, sommet, wherefield, wherefieldreppere) VALUES (?, ?, ?, false, ?, ?)";
      jdbcTemplate.update(sql, parentId, subReportId, level, wherefield, wherefieldreppere);

      return ResponseEntity.ok("Report assigned successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign report: " + e.getMessage());
    }
  }



  @GetMapping("/rapport_fields/{id}")
    public List<Map<String, Object>> getRapportFieldsByRapportId(@PathVariable Long id) {
        String sql = "SELECT field_name FROM etl.rep_rapports_x WHERE id_rapport = ?";
        return jdbcTemplate.queryForList(sql, id);
    }


  @GetMapping("/generateQuery")
  public String generateQuery() {
    // Call the generateQuery method from the QueryGenerationService
    return reportService.generateQuery();
  }


  @GetMapping("/generateData")
  public List<Map<String, Object>> generateData() {
    // Generate the query
    String query = reportService.generateQuery();

    // Generate data from the query
    return reportService.generateDataFromQuery(query);
  }

  @GetMapping("/initializeLastRepRapport")
  public RepRapport initializeLastRepRapport(){
    return reportService.initializeLastRepRapport();
  }
  @Autowired
  private ObjectMapper objectMapper; // Spring Boot will auto-configure this

  @PostMapping("/generatetestQuery")
  public String generatetestQuery(@RequestBody Map<String, Object> request) {
    // Extract the list of RepRapportsX from the request body
    List<RepRapportXDTO> repRapportsXList = objectMapper.convertValue(request.get("repRapportsXList"), new TypeReference<List<RepRapportXDTO>>() {});
    return reportService.generatetestQuery(repRapportsXList);
  }

  @RequestMapping(value="/generatereport", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ChartResp> generatereport(@RequestBody RapportDTO reportDTO) {
    try {
      ChartResp resp = reportService.generateChartfromquery(reportDTO);
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      ChartResp errorResp = new ChartResp();
      errorResp.setErroMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResp);
    }
  }


    @RequestMapping(value="/addC", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveReportCustomize(@RequestBody RapportDTO reportDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            reportService.saveReportCustomize(reportDTO);
            response.put("message", "Report saved successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to save report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @RequestMapping(value="/queryC", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getQueryCustomize(@RequestBody RapportDTO reportDTO) {
        try {
            String query = reportService.generateQueryFromDTOCustomize(reportDTO);
            return ResponseEntity.ok(query);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save report: " + e.getMessage());
        }
    }

    @RequestMapping(value="/getDataC", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChartResp> getDataC(@RequestBody RapportDTO reportDTO) {
        try {
            ChartResp resp = reportService.generateChartDataC(reportDTO);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            ChartResp errorResp = new ChartResp();
            errorResp.setErroMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResp);
        }
    }

}
