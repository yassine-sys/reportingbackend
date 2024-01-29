package com.example.backend.dao;

import com.example.backend.entities.MissingSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingSeqRepository extends JpaRepository<MissingSeq,Long> {
}
