package com.example.backend.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlaylistReportId implements Serializable {

    private Long playlistId;
    private Long reportId;

    // Constructors, getters, setters, hashCode, equals


    public PlaylistReportId() {
    }

    public PlaylistReportId(Long playlistId, Long reportId) {
        this.playlistId = playlistId;
        this.reportId = reportId;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistReportId that = (PlaylistReportId) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(reportId, that.reportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, reportId);
    }
}

