package com.example.backend.dao;

import com.example.backend.entities.Module;
import com.example.backend.entities.Monnaie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonnaieRepository extends JpaRepository<Monnaie,Long> {
}
