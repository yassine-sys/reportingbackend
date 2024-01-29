package com.example.backend.services;

import com.example.backend.entities.DupCdr;
import org.apache.logging.log4j.util.PerformanceSensitive;

import javax.persistence.EntityManager;
import java.util.List;

public class DupCdrServiceImpl implements DupCdrService{
    @PerformanceSensitive
    EntityManager em;
    @Override
    public List<DupCdr> getDupCdr(String date) {
        return null;
    }
}
