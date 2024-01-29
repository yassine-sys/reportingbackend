package com.example.backend.services;

import com.example.backend.dao.PlaylistReportRepository;
import com.example.backend.dao.PlaylistRepository;
import com.example.backend.dao.RepRapportRepository;
import com.example.backend.entities.PlaylistReport;
import com.example.backend.entities.RepRapport;
import com.example.backend.entities.PlayLIst;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final RepRapportRepository repRapportRepository;
    private final PlaylistReportRepository playlistReportRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, RepRapportRepository repRapportRepository,PlaylistReportRepository playlistReportRepository) {
        this.playlistRepository = playlistRepository;
        this.repRapportRepository = repRapportRepository;
        this.playlistReportRepository = playlistReportRepository;
    }

    public List<PlayLIst> getAllPlayList() {
        List<PlayLIst> playlists = playlistRepository.findAll();

        // Sorting the reports in each playlist
        for (PlayLIst playlist : playlists) {
            playlist.getPlaylistReports().sort(Comparator.comparing(PlaylistReport::getRep_order));
        }

        return playlists;
    }

    public void createPlayList(PlayLIst playLIst){
        playlistRepository.save(playLIst);
    }

    private int getNextOrderForPlaylist(PlayLIst playlist) {
        return playlist.getPlaylistReports().stream()
                .mapToInt(PlaylistReport::getRep_order)
                .max()
                .orElse(0) + 1;
    }

    @Transactional
    public void addReportToPlaylist(Long playlistId, Long reportId) throws NotFoundException {
        PlayLIst playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found with id: " + playlistId));

        RepRapport report = repRapportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report not found with id: " + reportId));

        //playlist.addReport(report);
        int nextOrder = getNextOrderForPlaylist(playlist);
        PlaylistReport p = new PlaylistReport();
        p.setReport(report);
        p.setPlaylist(playlist);
        p.setRep_order(nextOrder);
        playlistReportRepository.save(p);
    }

    public void addReportToMultiplePlaylists(Long reportId, List<Long> playlistIds) {
        // Find the report by its ID
        RepRapport report = repRapportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        // Iterate through the list of playlistIds and add the report to each playlist
        for (Long playlistId : playlistIds) {
            PlayLIst playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new EntityNotFoundException("Playlist not found with ID: " + playlistId));

            // Check if the report is already in the playlist
            if (!playlist.getPlaylistReports().contains(report)) {
                // Add the report to the playlist
                //playlist.addReport(report);
                int nextOrder = getNextOrderForPlaylist(playlist);
                PlaylistReport p = new PlaylistReport();
                p.setReport(report);
                p.setPlaylist(playlist);
                p.setRep_order(nextOrder);
                playlistReportRepository.save(p);
            }
        }
    }

    @Transactional
    public void deletePlaylist(Long playlistId) {
        PlayLIst playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with ID: " + playlistId));

        // Explicitly delete PlaylistReport entities
        for (PlaylistReport pr : playlist.getPlaylistReports()) {
            playlistReportRepository.delete(pr);
        }

        // Now it's safe to delete the playlist
        playlistRepository.delete(playlist);
    }


    @Transactional
    public void detachReportFromPlaylist(Long playlistId, Long reportId) {
        // Find the playlist and ensure it exists
        PlayLIst playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with ID: " + playlistId));

        // Find the specific PlaylistReport entity and delete it
        playlist.getPlaylistReports().stream()
                .filter(pr -> pr.getReport().getId().equals(reportId))
                .findFirst()
                .ifPresent(pr -> {
                    playlist.getPlaylistReports().remove(pr); // Remove from the collection
                    playlistReportRepository.delete(pr); // Delete the entity
                });
    }


    public PlayLIst getPlayListById(Long id){
        Optional<PlayLIst> p = playlistRepository.findPlayLIstById(id);
        return  p.get();
    }


    @Transactional
    public void updateReportOrderInPlaylist(Long playlistId, List<Long> orderedReportIds) throws NotFoundException {
        PlayLIst playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found with id: " + playlistId));

        List<PlaylistReport> playlistReports = playlist.getPlaylistReports();

        // Map of reportId to PlaylistReport
        Map<Long, PlaylistReport> reportMap = playlistReports.stream()
                .collect(Collectors.toMap(pr -> pr.getReport().getId(), pr -> pr));

        for (int i = 0; i < orderedReportIds.size(); i++) {
            Long reportId = orderedReportIds.get(i);
            PlaylistReport playlistReport = reportMap.get(reportId);

            if (playlistReport != null) {
                playlistReport.setRep_order(i + 1); // Set the new order
                playlistReportRepository.save(playlistReport);
            } else {
                throw new NotFoundException("Report not found in playlist: " + reportId);
            }
        }
    }


}
