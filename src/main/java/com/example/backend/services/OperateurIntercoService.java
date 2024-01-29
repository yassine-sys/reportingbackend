package com.example.backend.services;


import com.example.backend.entities.OperateurInterco;

import java.util.List;

public interface OperateurIntercoService {
    OperateurInterco getInterDestByPays(int id);
    List<OperateurInterco> getAll();
}
