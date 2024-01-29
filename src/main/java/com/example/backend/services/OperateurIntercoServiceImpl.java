package com.example.backend.services;

import com.example.backend.dao.OperateurIntercoRepository;
import com.example.backend.entities.OperateurInterco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateurIntercoServiceImpl implements OperateurIntercoService{
    @Autowired
    OperateurIntercoRepository operateurIntercoRepository;
    @Override
    public OperateurInterco getInterDestByPays(int id) {
        return operateurIntercoRepository.findOperateurIntercoById(id);
    }

    @Override
    public List<OperateurInterco> getAll() {
        return operateurIntercoRepository.findAll();
    }
}
