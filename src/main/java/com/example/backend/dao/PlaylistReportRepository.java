package com.example.backend.dao;

import com.example.backend.entities.PlaylistReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistReportRepository extends JpaRepository<PlaylistReport,Long> {
    Optional<PlaylistReport> findPlaylistReportByPlaylistId(Long id);
}
