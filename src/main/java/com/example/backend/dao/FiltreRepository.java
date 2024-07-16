package com.example.backend.dao;

import com.example.backend.entities.conf.Filtre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiltreRepository extends JpaRepository<Filtre, Long> {
    Filtre findByFiltreAndRepRapportId(String filtre, Long repRapportId);
}
