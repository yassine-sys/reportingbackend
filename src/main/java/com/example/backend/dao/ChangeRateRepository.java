package com.example.backend.dao;

import com.example.backend.entities.ChangeRate;
import com.example.backend.entities.Monnaie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeRateRepository extends JpaRepository<ChangeRate,Long> {
    @Query("SELECT cr FROM taux_change cr WHERE cr.id_monnaie = :id")
    ChangeRate findChangeRateById_monnaie(Long id);
}
