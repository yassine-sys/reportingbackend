package com.example.backend.services;

import com.example.backend.dao.InterDestRepository;
import com.example.backend.entities.InterDest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InterDestServiceImpl implements InterDestService{
    @Autowired
    InterDestRepository rep;
    @Override
    public List<InterDest> getAllDest() {
        return rep.findAll();
    }

    @Override
    public InterDest getInterDestByPays(int id) {
        return rep.findInterDestByIdPays(id);
    }
}
