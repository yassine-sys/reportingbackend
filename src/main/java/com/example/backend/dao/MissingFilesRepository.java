package com.example.backend.dao;

import com.example.backend.entities.MissingFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingFilesRepository extends JpaRepository<MissingFiles,String> {
}
