package com.example.backend.dao;

import com.example.backend.entities.InterDest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterDestRepository extends JpaRepository<InterDest,Long> {
    InterDest findInterDestByIdPays(int id);
}
