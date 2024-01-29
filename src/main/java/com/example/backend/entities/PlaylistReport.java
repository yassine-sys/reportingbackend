package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
@Entity(name = "playlist_Reports")
@Table(schema = "management")
public class PlaylistReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonIgnore
    private PlayLIst playlist;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private RepRapport report;

    @Column(name = "rep_order")
    private Integer rep_order;

    public PlayLIst getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlayLIst playlist) {
        this.playlist = playlist;
    }

    public RepRapport getReport() {
        return report;
    }

    public void setReport(RepRapport report) {
        this.report = report;
    }

    public Integer getRep_order() {
        return rep_order;
    }

    public void setRep_order(Integer rep_order) {
        this.rep_order = rep_order;
    }
}
