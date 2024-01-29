package com.example.backend.Controllers;

import com.example.backend.entities.PlayLIst;
import com.example.backend.entities.PlaylistReport;
import com.example.backend.services.PlaylistService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlayLIst> getAllPlayList(){
        return playlistService.getAllPlayList();
    }
    @PostMapping("/create")
    public ResponseEntity<String> addReportToPlaylist(@RequestBody PlayLIst playLIst) throws NotFoundException {
        playlistService.createPlayList(playLIst);

        return ResponseEntity.ok("Playlist created successfully.");
    }

    @PostMapping("/{playlistId}/addReport")
    public ResponseEntity<String> addReportToPlaylist(@PathVariable Long playlistId, @RequestBody Map<String, Long> requestBody) throws NotFoundException {
        Long reportId = requestBody.get("reportId");
        playlistService.addReportToPlaylist(playlistId, reportId);
        return ResponseEntity.ok("Report added to the playlist successfully.");
    }

    // Endpoint to add a report to multiple playlists
    @PostMapping("/addReportToMultiplePlaylists")
    public ResponseEntity<String> addReportToMultiplePlaylists(@RequestBody Map<String, Object> requestBody) {
        Object reportIdObj = requestBody.get("reportId");
        Object playlistIdsObj = requestBody.get("playlistIds");

        if (reportIdObj instanceof Number && playlistIdsObj instanceof List<?>) {
            Long reportId = ((Number) reportIdObj).longValue();
            List<Long> playlistIds = ((List<?>) playlistIdsObj).stream()
                    .map(id -> ((Number) id).longValue())
                    .collect(Collectors.toList());

            playlistService.addReportToMultiplePlaylists(reportId, playlistIds);

            return ResponseEntity.ok("Report added to multiple playlists successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid request format.");
        }
    }
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.ok("Playlist deleted successfully.");
    }


    @DeleteMapping("/{playlistId}/detachReport/{reportId}")
    public ResponseEntity<String> detachReportFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long reportId) {
        playlistService.detachReportFromPlaylist(playlistId, reportId);
        return ResponseEntity.ok("Report detached from the playlist successfully.");
    }
    @GetMapping("/{id}")
    public PlayLIst getPlayListById(@PathVariable Long id){
        return playlistService.getPlayListById(id);
    }

    @PutMapping("/{playlistId}/updateOrder")
    public ResponseEntity<?> updatePlaylistOrder(@PathVariable Long playlistId, @RequestBody List<Long> orderedReportIds) {
        try {
            playlistService.updateReportOrderInPlaylist(playlistId, orderedReportIds);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
