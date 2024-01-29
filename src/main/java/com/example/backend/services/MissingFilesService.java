package com.example.backend.services;

import com.example.backend.entities.MissingFiles;
import com.example.backend.entities.MissingSeq;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MissingFilesService {
    List<MissingFiles> getAllMissingFiles();
    List<MissingSeq> getAllMissingSeq();

}
