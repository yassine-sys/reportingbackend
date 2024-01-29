package com.example.backend.services;

import com.example.backend.entities.EtatCollect;

import java.util.List;

public interface EtatCollectService {

    List<EtatCollect> getEtatCollectByDate(String date);
}
