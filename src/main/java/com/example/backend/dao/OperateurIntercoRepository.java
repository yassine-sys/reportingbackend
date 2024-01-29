package com.example.backend.dao;

import com.example.backend.entities.OperateurInterco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateurIntercoRepository extends JpaRepository<OperateurInterco,Long> {
    OperateurInterco findOperateurIntercoById(int id);

}
