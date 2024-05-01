package com.example.backend.Controllers;

import com.example.backend.dao.RepRapportRepository;
import com.example.backend.entities.DTOResp.RapportDTO;
import com.example.backend.entities.DTOResp.ReportDTO;
import com.example.backend.entities.RepRapport;
import com.example.backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/rapport")
public class RepRapportController {
    @Autowired
    RepRapportRepository repRepo;

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
}
