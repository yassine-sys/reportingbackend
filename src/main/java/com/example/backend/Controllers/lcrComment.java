package com.example.backend.Controllers;

import com.example.backend.entities.commentlcr;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/lcr")
public class lcrComment {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addComment(@RequestBody commentlcr c) {
        try {
            String sql = "INSERT INTO tableref.commentlcr (lcroperator, destinationoperator, comment) " +
                    "VALUES (?, ?, ?)";

            entityManager.createNativeQuery(sql)
                    .setParameter(1, c.getLcroperator())
                    .setParameter(2, c.getDestinationoperator())
                    .setParameter(3, c.getComment())
                    .executeUpdate();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

