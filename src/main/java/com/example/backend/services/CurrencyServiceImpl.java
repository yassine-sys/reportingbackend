package com.example.backend.services;

import com.example.backend.dao.ChangeRateRepository;
import com.example.backend.dao.MonnaieRepository;
import com.example.backend.entities.ChangeRate;
import com.example.backend.entities.Monnaie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CurrencyServiceImpl implements CurrencyInterface{
    @Autowired
    ChangeRateRepository changeRateRepository;

    @Autowired
    MonnaieRepository monnaieRepository;

    @Override
    public List<Monnaie> getAllCurencies() {
        return monnaieRepository.findAll();
    }

    @Override
    public List<ChangeRate> getAllChangeRate() {
        return changeRateRepository.findAll();
    }

    @Override
    public ChangeRate findChangeRate(Long id) {
        return changeRateRepository.findChangeRateById_monnaie(id);
    }
}
