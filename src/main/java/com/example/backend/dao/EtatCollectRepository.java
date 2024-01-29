package com.example.backend.dao;

import com.example.backend.entities.EtatCollect;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EtatCollectRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<EtatCollect> findByDate(String date) {
        String sql = "SELECT date_collect, name_flow , name_rep , nb_files, nb_records, files_processed, nb_corrupted, average FROM tables.stat_collect WHERE date_collect = ? ORDER BY date_collect desc";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, date);

        List<Object[]> resultRows = query.getResultList();
        List<EtatCollect> etatCollectData = new ArrayList<>();

        for (Object[] row : resultRows) {
            EtatCollect etatCollectDTO = new EtatCollect();
            etatCollectDTO.setDate((String) row[0]);
            etatCollectDTO.setName_flow((String) row[1]);
            etatCollectDTO.setName_rep((String) row[2]);
            etatCollectDTO.setNbfiles((Integer) row[3]);
            etatCollectDTO.setNbrecord((Integer) row[4]);
            etatCollectDTO.setNbfileprocessed((Integer) row[5]);
            etatCollectDTO.setNbfilecorrupted((Integer) row[6]);
            etatCollectDTO.setAverage((Integer) row[7]);

            etatCollectData.add(etatCollectDTO);
        }

        return etatCollectData;
    }

    public List<EtatCollect> getAll() {
        String sql = "SELECT date_collect, name_flow, name_rep , nb_files, nb_records, files_processed, nb_corrupted, average FROM tables.stat_collect ORDER BY date_collect desc";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> resultRows = query.getResultList();
        List<EtatCollect> etatCollectData = new ArrayList<>();

        for (Object[] row : resultRows) {
            EtatCollect etatCollectDTO = new EtatCollect();
            etatCollectDTO.setDate((String) row[0]);
            etatCollectDTO.setName_flow((String) row[1]);
            etatCollectDTO.setName_rep((String) row[2]);
            etatCollectDTO.setNbfiles((Integer) row[3]);
            etatCollectDTO.setNbrecord((Integer) row[4]);
            etatCollectDTO.setNbfileprocessed((Integer) row[5]);
            etatCollectDTO.setNbfilecorrupted((Integer) row[6]);
            etatCollectDTO.setAverage((Integer) row[7]);
            etatCollectData.add(etatCollectDTO);

            System.out.println(etatCollectDTO.getName_flow());
        }

        return etatCollectData;
    }


}

