package com.example.backend.services;

import com.example.backend.entities.DupCdr;

import java.util.List;

public interface DupCdrService {
    List<DupCdr> getDupCdr(String date);
}
