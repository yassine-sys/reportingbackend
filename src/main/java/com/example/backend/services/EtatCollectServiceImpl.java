package com.example.backend.services;

import com.example.backend.entities.EtatCollect;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("EtatCollectService")
public class EtatCollectServiceImpl implements EtatCollectService{
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


}
