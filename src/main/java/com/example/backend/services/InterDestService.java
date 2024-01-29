package com.example.backend.services;

import com.example.backend.entities.InterDest;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface InterDestService {
   List<InterDest> getAllDest();
   InterDest getInterDestByPays(int id);
}
