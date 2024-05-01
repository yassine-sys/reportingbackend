package com.example.backend.services;

import com.example.backend.entities.EtatCollect;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.example.backend.entities.MapInfoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service("EtatCollectService")
public class EtatCollectServiceImpl implements EtatCollectService {
    @PersistenceContext
    private EntityManager entityManager;

//    @Autowired
//    EtatColelctRepository etatColelctRepository;

    @Override
    public List<EtatCollect> getEtatCollectByDate(String date) {
        String sql = "SELECT date, name_flow, nbfiles, nbrecord, nbfileprocessed, nbfilecorrupted, average ,frequency" +
                "FROM stat.etat_collect WHERE date = :date";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("date", date);

        List<EtatCollect> resultList = query.getResultList();
        return resultList;
    }


    @Override
    public List<MapInfoDTO> getMapInfo(String startDate, String endDate, String typeCall, String order, Integer limit,String typeCdr,String tablename) {
        String sql = "SELECT t1.site_id, " +
                "AVG(CAST(t1.latitude AS NUMERIC)) AS latitude, " +
                "AVG(CAST(t1.longitude AS NUMERIC)) AS longitude, " +
                "CAST(SUM(coalesce(duration,0)) / 60.0 AS FLOAT) AS duration, " +
                "SUM(CAST(t2.count AS INTEGER)) AS call_count, " +
                "SUM(CAST(t2.subscribers AS INTEGER)) AS subscribers, " +
                "coalesce(t1.technology,'undefinded') AS technolgy " +
                "FROM tableref.cell_sites t1 " +
                "JOIN "+tablename+" t2 ON SUBSTRING(t1.cgi, 8) = t2.LOCATION " +
                "WHERE t2.type_call = :typeCall " +
                "AND SUBSTRING(t2.answertime, 1, 6) >= :startDate " +
                "AND SUBSTRING(t2.answertime, 1, 6) <= :endDate " +
                "AND type_cdr = :typecdr " +
                "GROUP BY t1.site_id , t1.technology " +
                "ORDER BY subscribers " + order + " LIMIT " + limit;

        // Create native query and set parameter
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("typeCall", typeCall);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("typecdr", typeCdr);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(result -> new MapInfoDTO(
                        (String) result[0], // site_id
                        (BigDecimal) result[1], // latitude
                        (BigDecimal) result[2], // longitude
                        ((Number) result[3]).doubleValue(), // duration, safely converting to Integer
                        ((Number) result[4]).intValue(), // call_count, safely converting to Integer
                        ((Number) result[5]).intValue(),// subscribers, safely converting to Integer
                        (String) result[6] //technology
                ))

                .collect(Collectors.toList());
    }


    @Override
    public List<MapInfoDTO> getMapInfoV2(String startDate, String endDate, String typeCall, String order, Integer limit,String typeCdr) {
        String sql = "SELECT split_part(t1.cell_names,'-',1) AS site_id, " +
                "AVG(CAST(t1.latitude AS NUMERIC)) AS latitude, " +
                "AVG(CAST(t1.longitude AS NUMERIC)) AS longitude, " +
                "CAST(SUM(coalesce(callduration,0)) / 60.0 AS FLOAT) AS duration, " +
                "SUM(CAST(t2.count AS INTEGER)) AS call_count, " +
                "SUM(CAST(t2.subscribers_count AS INTEGER)) AS subscribers ," +
                "coalesce(t1.site_type,'undefined') AS technolgy " +
                "FROM tableref.cell_sites t1 " +
                "JOIN stat.stat_msc_location t2 ON  lac || '-' || ci = t2.LOCATION " +
                "WHERE t2.type_call = :typeCall " +
                "AND SUBSTRING(t2.answertime, 1, 6) >= :startDate " +
                "AND SUBSTRING(t2.answertime, 1, 6) <= :endDate " +
                "AND type_cdr = :typecdr " +
                "GROUP BY site_id ,t1.site_type  " +
                "ORDER BY subscribers " + order;

        if(limit != -1){
            sql += " LIMIT " + limit;
        }

        // Create native query and set parameter
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("typeCall", typeCall);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("typecdr", typeCdr);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(result -> new MapInfoDTO(
                        (String) result[0], // site_id
                        (BigDecimal) result[1], // latitude
                        (BigDecimal) result[2], // longitude
                        ((Number) result[3]).doubleValue(), // duration, safely converting to Integer
                        ((Number) result[4]).intValue(), // call_count, safely converting to Integer
                        ((Number) result[5]).intValue(),// subscribers, safely converting to Integer
                        (String) result[6] //technology
                ))

                .collect(Collectors.toList());
    }

}
