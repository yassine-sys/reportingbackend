package com.example.backend.dao;

import com.example.backend.entities.PlayLIst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<PlayLIst,Long> {
    Optional<PlayLIst> findPlayLIstById(Long id);
}
