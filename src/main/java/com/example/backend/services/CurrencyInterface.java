package com.example.backend.services;

import com.example.backend.entities.ChangeRate;
import com.example.backend.entities.Monnaie;

import java.util.List;

public interface CurrencyInterface {
    List<Monnaie> getAllCurencies();
    List<ChangeRate> getAllChangeRate();

    ChangeRate findChangeRate(Long id);
}
