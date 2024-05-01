package com.example.backend.Controllers;

import com.example.backend.dao.EtatCollectRepository;
import com.example.backend.dao.MissingFilesRepository;
import com.example.backend.dao.MissingSeqRepository;
import com.example.backend.entities.*;
import com.example.backend.services.EtatCollectService;
import com.example.backend.services.MissingFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
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
        String queryStr = "SELECT COUNT(*) FROM cdrs_archives." + tableName + " WHERE callduration > 0";
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

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value = "/missingFilesRec", method = RequestMethod.GET)
    public ResponseEntity<List<Object[]>> getMissingFilesRecords() {
        try {
            // Execute the query
            Query query = entityManager.createNativeQuery("SELECT * FROM stat.Stat_Daily_Misisng_files_recon where dur_swt>0 or dur>0 order by date_switch desc");
            List<Object[]> results = query.getResultList();
            //List<MissingFilesRec> missingFilesRecDTOs = mapToDTO(results);

            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<MissingFilesRec> mapToDTO(List<Object[]> results) {
        List<MissingFilesRec> missingFilesRecDTOs = new ArrayList<>();

        for (Object[] result : results) {
            MissingFilesRec dto = new MissingFilesRec();
            dto.setDate_ixtools((String) result[0]);
            dto.setCdrfilename((String) result[1]);
            dto.setCdr_countix((Integer) result[2]);
            dto.setDur((BigDecimal) result[3]);
            dto.setDate_switch((String) result[4]);
            dto.setFilename((String) result[5]);
            dto.setDur_swt((BigDecimal) result[6]);
            dto.setCdr_countswt((Integer) result[7]);

            missingFilesRecDTOs.add(dto);
        }

        return missingFilesRecDTOs;
    }

    @RequestMapping(value = "/margin/{country}/{beginDate}/{endDate}", method = RequestMethod.GET)
    public ResponseEntity<?> getMargin(@PathVariable(name = "country") String country,
                                       @PathVariable(name = "beginDate") String beginDate,
                                       @PathVariable(name = "endDate") String endDate) {
        String queryString = String.format("SELECT callingnumber,callednumber,networkcallreference,type_call,mscincomingroute,mscoutgoingroute,answertime, callduration,tarif_class,subscription_type,country_name,operator_name,rate,amount,rate_litc,amount_litc,rating_method_madar,rating_method,id_outgoing_operator,id_incoming_operator FROM tab_temp.margin_dynamic_view WHERE country_name = '%s' AND substr(answertime,1,6) >= '%s' AND substr(answertime,1,6) <= '%s';", country, beginDate, endDate);
        try {
            List<Object[]> result = entityManager.createNativeQuery(queryString).getResultList();
            if (result == null || result.isEmpty()) {
                // Return an empty object or list, as appropriate
                return ResponseEntity.ok().body(new Object[]{});
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }

    //almadar
    @RequestMapping(value = "/map/{startDate}/{endDate}/{typeCall}/{order}/{limit}/{typeCdr}", method = RequestMethod.GET)
    public List<MapInfoDTO> getMapInfo(@PathVariable(name = "startDate") String startDate,
                                       @PathVariable(name = "endDate") String endDate,
                                       @PathVariable(name = "typeCall") String typeCall,
                                       @PathVariable(name = "order") String order,
                                       @PathVariable(name = "limit") Integer limit,
                                       @PathVariable(name = "typeCdr") String typeCdr) {
        String tablename="";
        if(typeCdr.equals("natroa")){
            tablename = "stat.stat_msc_location";
        }else{
            tablename = "stat.msc_location_not_roa";
        }
        return etatCollectService.getMapInfo( startDate, endDate, typeCall, order,limit,typeCdr,tablename);
    }


    //libyana
    @RequestMapping(value = "/maps/{startDate}/{endDate}/{typeCall}/{order}/{limit}/{typeCdr}", method = RequestMethod.GET)
    public List<MapInfoDTO> getMapInfoV2(@PathVariable(name = "startDate") String startDate,
                                       @PathVariable(name = "endDate") String endDate,
                                       @PathVariable(name = "typeCall") String typeCall,
                                       @PathVariable(name = "order") String order,
                                         @PathVariable(name = "limit") String limitStr,
                                         @PathVariable(name = "typeCdr") String typeCdr) {

        Integer limit;
        if ("null".equalsIgnoreCase(limitStr)) {
            limit = -1; // Set limit to -1 when "null" is passed
        } else {
            limit = Integer.parseInt(limitStr);
        }
        return etatCollectService.getMapInfoV2( startDate, endDate, typeCall, order,limit,typeCdr);
    }



}


