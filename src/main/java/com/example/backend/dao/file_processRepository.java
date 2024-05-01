package com.example.backend.dao;

import com.example.backend.entities.files_process;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class file_processRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<files_process> getdetails(int id_flow, String date_reception, String name_rep,String switche) {
        String idRepQuery = "SELECT id FROM etl.etl_repository WHERE rep_name = :name_rep";
        BigInteger id_rep = (BigInteger) entityManager
                .createNativeQuery(idRepQuery)
                .setParameter("name_rep", name_rep)
                .getSingleResult();

        String fileProcessQuery = "SELECT * FROM tables.files_process_" + id_flow + " WHERE TO_CHAR(DATE(date_reception), 'YYYY-MM-DD') LIKE ? AND id_rep = ? AND substr(filename,1,3) = ?";
        List<Object[]> resultList = entityManager
                .createNativeQuery(fileProcessQuery)
                .setParameter(1, date_reception + "%")
                .setParameter(2, id_rep)
                .setParameter(3,switche)
                .getResultList();

        List<files_process> liste = new ArrayList<>();
        for(Object[] row :resultList){
            files_process fp = new files_process();
            fp.setFilename((String) row[0]);
            Timestamp timestamp = (Timestamp) row[1];
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp);
            fp.setDate_reception(formattedDate);
            fp.setStatut((Integer) row[2]);
            fp.setId_rep((BigInteger) row[3]);
            fp.setId_rep_fils((BigInteger) row[4]);
            fp.setNb_record((Integer) row[5]);
            liste.add(fp);
        }



        return liste;
    }


}
