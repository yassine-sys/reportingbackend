package com.example.backend.services;

import com.example.backend.entities.EtatCollect;
import com.example.backend.entities.MapInfoDTO;

import java.util.List;

public interface EtatCollectService {

    List<EtatCollect> getEtatCollectByDate(String date);

    List<MapInfoDTO> getMapInfo(String startDate,String endDate,String typeCall,String order,Integer limit,String typeCdr,String tablename);

    List<MapInfoDTO> getMapInfoV2(String startDate, String endDate, String typeCall, String order, Integer limit,String typeCdr);
}
