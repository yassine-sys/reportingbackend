package com.example.backend.Controllers;

import com.example.backend.dao.file_processRepository;
import com.example.backend.entities.DTOResp.FileProcessDTO;
import com.example.backend.entities.files_process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileprocessController {

    @Autowired
    private file_processRepository fileprocessrepo;

    @GetMapping("/details/{id_flow}/{date_reception}/{name_rep}/{switche}")
    public List<files_process> getDetails(
            @PathVariable int id_flow,
            @PathVariable String date_reception,
            @PathVariable String name_rep,
            @PathVariable String switche
    ) {
        return fileprocessrepo.getdetails(id_flow, date_reception, name_rep,switche);
    }
}
