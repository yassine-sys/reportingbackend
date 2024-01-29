package com.example.backend.Controllers;

import com.example.backend.dao.EtatCollectRepository;
import com.example.backend.dao.MissingFilesRepository;
import com.example.backend.dao.MissingSeqRepository;
import com.example.backend.entities.*;
import com.example.backend.services.EtatCollectService;
import com.example.backend.services.MissingFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/cdr")
public class CdrController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    EtatCollectService etatCollectService;

    @Autowired
    private EtatCollectRepository etatCollectRepository;

    @Autowired
    MissingFilesService missingFilesService;


    private static final String TABLE_NAME_PREFIX_INTERCO = "interco";
    private static final String TABLE_NAME_PREFIX_IXTOOLS = "ixtools";

    private static final String TABLE_NAME_PREFIX_MSC="cdrs_msc";
    private static final String TABLE_NAME_PREFIX_OCS="cdrs_ocs";

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public CdrCountResponse countCdrs() {
//        // Get today's date
//        LocalDate today = LocalDate.now();
//
//        // Get yesterday's date
//        LocalDate yesterday = today;
//
//        // Format dates as "yyMMdd"
//        String formattedToday = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
//        String formattedYesterday = yesterday.format(DateTimeFormatter.ofPattern("yyMMdd"));
//
//        // Create the table names for today and yesterday
//        String todayTableNameInterco = getLatestTableName(TABLE_NAME_PREFIX_INTERCO, formattedToday);
//        String todayTableNameIxTools = getLatestTableName(TABLE_NAME_PREFIX_IXTOOLS, formattedToday);
//        String yesterdayTableNameInterco = getLatestTableName(TABLE_NAME_PREFIX_INTERCO, formattedYesterday);
//        String yesterdayTableNameIxTools = getLatestTableName(TABLE_NAME_PREFIX_IXTOOLS, formattedYesterday);
//
//        // Check if the latest table for today is not found, get the previous day's table
//        if (todayTableNameInterco == null || todayTableNameIxTools == null || todayTableNameInterco.equals(getLatestTableName(TABLE_NAME_PREFIX_INTERCO, today.format(DateTimeFormatter.ofPattern("yyMMdd")))) || todayTableNameIxTools.equals(getLatestTableName(TABLE_NAME_PREFIX_IXTOOLS, today.format(DateTimeFormatter.ofPattern("yyMMdd"))))) {
//            todayTableNameInterco = yesterdayTableNameInterco;
//            todayTableNameIxTools = yesterdayTableNameIxTools;
//        }
//
//        // Get the count for today's interco table
//        int countInterco = getCountFromUserTable(todayTableNameInterco);
//
//        // Get the count for today's ixtolls table
//        int countIxTools = getCountFromUserTable(todayTableNameIxTools);
//
//        // Format the dates as "yyyy-MM-dd"
//        String formattedTodayDate = formatDateString(todayTableNameInterco.substring(TABLE_NAME_PREFIX_INTERCO.length()));
//        String formattedCountDate = formatDateString(todayTableNameIxTools.substring(TABLE_NAME_PREFIX_IXTOOLS.length()));
//
//        // Create and return the response object
//        CdrCountResponse response = new CdrCountResponse(formattedTodayDate, formattedCountDate, countInterco, countIxTools);
//        return response;
//    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/ix_inter", method = RequestMethod.GET)
    public IxInterResp countCdrsIxInter() {
        IxInterResp resp = new IxInterResp();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        String formattedToday = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String formattedYesterday = yesterday.format(DateTimeFormatter.ofPattern("yyMMdd"));

        resp.setTodayDate(formattedToday);

        String todayTableNameInterco = getLatestTableName2("interco", formattedToday,"interco"+formattedToday);
        String todayTableNameIxTools = getLatestTableName2("ixtools", formattedToday,"ixtools"+formattedToday);
        String yesterdayTableNameInterco = getLatestTableName2("interco", formattedYesterday,todayTableNameInterco);
        String yesterdayTableNameIxTools = getLatestTableName2("ixtools", formattedYesterday,todayTableNameIxTools);

        Interco msc = new Interco();
        IxTools ocs = new IxTools();

        if (todayTableNameInterco != null) {
                int countMscToday = getCountFromTable(todayTableNameInterco);
                msc.setCountIntercoToday(countMscToday);
                msc.setLatestInterco(formatDateString(todayTableNameInterco.substring("interco".length())));
        }

        if (yesterdayTableNameInterco != null) {
            int countMscYesterday = getCountFromTable(yesterdayTableNameInterco);
            msc.setCountIntercoYesterday(countMscYesterday);
            msc.setYesterdayIntercoDate(formatDateString(yesterdayTableNameInterco.substring("interco".length())));
        }

        if (todayTableNameIxTools != null) {
                int countOcsToday = getCountFromTableIxTools(todayTableNameIxTools);
                ocs.setCountIxToolsToday(countOcsToday);
                ocs.setLatestIxTools(formatDateString(todayTableNameIxTools.substring("ixtools".length())));
        }

        if(yesterdayTableNameIxTools!=null){
            int countOcsToday = getCountFromTableIxTools(yesterdayTableNameIxTools);
            ocs.setCountIxToolsYesterday(countOcsToday);
            ocs.setYesterdayIxToolsDate(formatDateString(yesterdayTableNameIxTools.substring("ixtools".length())));
        }
        resp.setInterco(msc);
        resp.setIxTools(ocs);

        return resp;
    }


    @PreAuthorize("hasRole('Admin')")

    @RequestMapping(value = "/msc_ocs", method = RequestMethod.GET)
    public CdrResp countCdrsMscOcs() {
        CdrResp resp = new CdrResp();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        String formattedToday = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String formattedYesterday = yesterday.format(DateTimeFormatter.ofPattern("yyMMdd"));

        resp.setTodayDate(formattedToday);

        String todayTableNameMsc = getTodayTableName("cdrs_msc", formattedToday,"cdrs_msc"+formattedToday);
        String todayTableNameOcs = getTodayTableName("cdrs_ocs", formattedToday,"cdrs_ocs"+formattedToday);
        String yesterdayTableNameMsc = getLatestTableName2("cdrs_msc", formattedYesterday,todayTableNameMsc);
        String yesterdayTableNameOcs = getLatestTableName2("cdrs_ocs", formattedYesterday,todayTableNameOcs);

        Msc msc = new Msc();
        Ocs ocs = new Ocs();

        if (todayTableNameMsc != null) {
            int countMscToday = getCountFromUserTable(todayTableNameMsc);
            msc.setCountMscToday(countMscToday);
            msc.setLatestMsc(formatDateString(todayTableNameMsc.substring("cdrs_msc".length())));
        }

        if (yesterdayTableNameMsc != null) {
            int countMscYesterday = getCountFromUserTable(yesterdayTableNameMsc);
            msc.setCountMscYesterday(countMscYesterday);
            msc.setYesterdayMscDate(formatDateString(yesterdayTableNameMsc.substring("cdrs_msc".length())));
        }

        if (todayTableNameOcs != null) {
            int countOcsToday = getCountFromUserTable(todayTableNameOcs);
            ocs.setCountOcsToday(countOcsToday);
            ocs.setLatestOcsDate(formatDateString(todayTableNameOcs.substring("cdrs_ocs".length())));
        }

        if (yesterdayTableNameOcs != null) {
            int countOcsYesterday = getCountFromUserTable(yesterdayTableNameOcs);
            ocs.setCountOcsYesterday(countOcsYesterday);
            ocs.setYestesrdayOcsDate(formatDateString(yesterdayTableNameOcs.substring("cdrs_ocs".length())));
        }

        resp.setMsc(msc);
        resp.setOcs(ocs);

        return resp;
    }
    private String formatDateString(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        return date.format(outputFormatter);
    }

    private String getLatestTableName(String tableNamePrefix, String formattedDate, String latestTableName) {
        String queryStr = "SELECT TABLE_NAME " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_NAME LIKE '" + tableNamePrefix + "%' " +
                "  AND TABLE_NAME < '" + tableNamePrefix + formattedDate + "' " +
                "ORDER BY TABLE_NAME DESC LIMIT 1";

        String resultTableName = null;

        do {
            Query query = entityManager.createNativeQuery(queryStr);
            Object result = query.getSingleResult();

            if (result != null) {
                resultTableName = result.toString();

                // Assuming the date part is at the end of the tableName
                int dateIndex = resultTableName.length() - formattedDate.length();

                // Extract the date suffix
                String dateSuffix = resultTableName.substring(dateIndex);

                // Decrement the date suffix by 1
                int newDate = Integer.parseInt(dateSuffix) - 1;
                System.out.println("===============================>"+newDate);

                // Replace the old date suffix with the new one
                resultTableName = resultTableName.substring(0, dateIndex) + String.format("%06d", newDate);
                System.out.println("===============================>"+resultTableName);
            } else {
                break; // Break the loop if there is no result
            }

        } while (resultTableName != null && resultTableName.compareTo(latestTableName) >= 0);

        return resultTableName;
    }


    private String getLatestTableName2(String tableNamePrefix, String formattedDate, String latestTableName) {
        String queryStr = "SELECT TABLE_NAME " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_NAME LIKE '" + tableNamePrefix + "%' " +
                "  AND TABLE_NAME < '" + latestTableName + "' " +
                "ORDER BY TABLE_NAME DESC LIMIT 1";

            Query query = entityManager.createNativeQuery(queryStr);
            Object result = query.getSingleResult();
            String resultTableName = result.toString();
        return resultTableName;
    }

    private String getTodayTableName(String tableNamePrefix, String formattedDate, String latestTableName) {
        String queryStr = "SELECT TABLE_NAME " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_NAME LIKE '" + tableNamePrefix + "%' " +
                "  AND TABLE_NAME <= '" + latestTableName + "' " +
                "ORDER BY TABLE_NAME DESC LIMIT 1";

        Query query = entityManager.createNativeQuery(queryStr);
        Object result = query.getSingleResult();
        String resultTableName = result.toString();
        return resultTableName;
    }



    private int getCountFromTable(String tableName) {
        if (tableName == null) {
            return 0;
        }

        // Prepare the SQL query
        String queryStr = "SELECT COUNT(*) FROM cdrs_archives." + tableName + " WHERE networkcallreference = 'detailed'";
        Query query = entityManager.createNativeQuery(queryStr);

        // Execute the query and retrieve the count
        Object result = query.getSingleResult();
        return result != null ? ((Number) result).intValue() : 0;
    }

    private int getCountFromTableIxTools(String tableName) {
        if (tableName == null) {
            return 0;
        }

        // Prepare the SQL query
        String queryStr = "SELECT COUNT(*) FROM cdrs_archives." + tableName ;
        Query query = entityManager.createNativeQuery(queryStr);

        // Execute the query and retrieve the count
        Object result = query.getSingleResult();
        return result != null ? ((Number) result).intValue() : 0;
    }

    private int getCountFromUserTable(String tableName) {
        if (tableName == null) {
            return 0;
        }

        String queryStr = "SELECT n_live_tup FROM pg_stat_user_tables WHERE schemaname = 'cdrs_archives' AND relname = '" + tableName + "'";
        Query query = entityManager.createNativeQuery(queryStr);

        Object result = query.getSingleResult();
        return result != null ? Integer.parseInt(result.toString()) : 0;
    }


    /*Etat Collect By day*/
//    @RequestMapping(value = "/etat-collect", method = RequestMethod.GET)
//    public List<EtatCollect> collectbyDay() {
//        Date currentDate = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
//        String formattedDate = dateFormat.format(currentDate);
//        System.out.println("Formatted Date: " + formattedDate);
//        return etatCollectService.getEtatCollectByDate(formattedDate);
//    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/etat-collect", method = RequestMethod.GET)
    public ResponseEntity<List<EtatCollect>> getEtatCollectByDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = yesterday.format(dateFormat);
        System.out.println(formattedDate);
        List<EtatCollect> etatCollectData = etatCollectRepository.findByDate(formattedDate);

        if (etatCollectData.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(etatCollectData);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/today-etat-collect", method = RequestMethod.GET)
    public ResponseEntity<List<EtatCollect>> getEtatCollectByDateToday() {
        LocalDate yesterday = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = yesterday.format(dateFormat);
        System.out.println(formattedDate);
        List<EtatCollect> etatCollectData = etatCollectRepository.findByDate(formattedDate);

        if (etatCollectData.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(etatCollectData);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/etat-collect-monthly", method = RequestMethod.GET)
    public ResponseEntity<List<EtatCollect>> getEtatCollectMonthly() {
        LocalDate yesterday = LocalDate.now().minusDays(30);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = yesterday.format(dateFormat);
        System.out.println(formattedDate);
        List<EtatCollect> etatCollectData = etatCollectRepository.findByDate(formattedDate);

        if (etatCollectData.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(etatCollectData);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    public ResponseEntity<List<EtatCollect>> getAllEtatCollect() {

        List<EtatCollect> etatCollectData = etatCollectRepository.getAll();

        if (etatCollectData.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(etatCollectData);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/today-collect", method = RequestMethod.GET)
    public ResponseEntity<List<EtatCollect>> getTodayEtatCollect() {

        List<EtatCollect> etatCollectData = etatCollectRepository.getAll();

        if (etatCollectData.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(etatCollectData);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/missingFiles", method = RequestMethod.GET)
    public ResponseEntity<List<MissingFilesDTO>> getAllMissingFiles(){
        List<MissingFilesDTO> resp = new ArrayList<>();
        List<MissingFiles> missingFiles = missingFilesService.getAllMissingFiles();
        for (MissingFiles m:missingFiles) {
            Query query = entityManager.createNativeQuery("select name from etl.etl_flows where id=:id");
            query.setParameter("id",m.getId_flow());
            String flowName = (String) query.getSingleResult();
            System.out.println(flowName);

            Query query2 = entityManager.createNativeQuery("select rep_name from etl.etl_repository where id=:id");
            query2.setParameter("id",m.getId_rep());
            String repName = (String) query2.getSingleResult();
            System.out.println(repName);

            MissingFilesDTO missingFilesDTO = new MissingFilesDTO();
            missingFilesDTO.setFilename(m.getFilename());
            missingFilesDTO.setFlow(flowName);
            missingFilesDTO.setName_rep(repName);

            resp.add(missingFilesDTO);
        }
        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/missingSequence", method = RequestMethod.GET)
    public ResponseEntity<List<MissingSeqDTO>>  getAllMissingSequence(){

        List<MissingSeqDTO> resp = new ArrayList<>();
        List<MissingSeq> missingSeqs = missingFilesService.getAllMissingSeq();
        for(MissingSeq m : missingSeqs){
            Query query = entityManager.createNativeQuery("select name from etl.etl_flows where id=:id");
            query.setParameter("id",m.getId_flow());
            String flowName = (String) query.getSingleResult();
            MissingSeqDTO missingSeqDTO = new MissingSeqDTO();

            missingSeqDTO.setDate_detection(m.getDate_detection());
            missingSeqDTO.setFlowName(flowName);
            missingSeqDTO.setMissedseq(m.getMissedseq());
            missingSeqDTO.setNode(m.getNode());
            missingSeqDTO.setNextrcvdfile(m.getNextrcvdfile());
            missingSeqDTO.setId_rep(m.getId_rep());
            missingSeqDTO.setRep_name(m.getRep_name());

            resp.add(missingSeqDTO);
        }
        return ResponseEntity.ok(resp);
    }

}
