package com.example.backend.services;

import com.example.backend.dao.MissingFilesRepository;
import com.example.backend.dao.MissingSeqRepository;
import com.example.backend.entities.MissingFiles;
import com.example.backend.entities.MissingFilesRec;
import com.example.backend.entities.MissingSeq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class MissingFilesServiceImpl implements MissingFilesService{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    MissingFilesRepository missingFilesRepository;
    @Autowired
    MissingSeqRepository missingSeqRepository;
    @Override
    public List<MissingFiles> getAllMissingFiles() {
        return missingFilesRepository.findAll();
    }

    public List<MissingSeq> getAllMissingSeq(){
        return missingSeqRepository.findAll();
    }


}
