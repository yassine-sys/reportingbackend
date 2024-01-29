package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "playList")
@Table(schema = "management")
public class PlayLIst {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String playListName;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistReport> playlistReports = new ArrayList<>();


    public PlayLIst() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

//    // Add a method to add a report to the playlist
//    public void addReport(RepRapport report) {
//        if (listreprapport == null) {
//            listreprapport = new ArrayList<>();
//        }
//        listreprapport.add(report);
//        report.getPlaylists().add(this);
//    }
//
//    // Add a method to remove a report from the playlist
//    public void removeReport(RepRapport report) {
//        if (listreprapport != null) {
//            listreprapport.remove(report);
//            report.getPlaylists().remove(this);
//        }
//    }
//
//    public List<RepRapport> getListreprapport() {
//        return listreprapport;
//    }
//
//    public void setListreprapport(List<RepRapport> listreprapport) {
//        this.listreprapport = listreprapport;
//    }

    public List<PlaylistReport> getPlaylistReports() {
        return playlistReports;
    }

    public void setPlaylistReports(List<PlaylistReport> playlistReports) {
        this.playlistReports = playlistReports;
    }
}
