package com.apollotune.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
/**
 * @author github.com/YusufDagdeviren"
 * @version 1.0
 * @since 2024-02-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String songname;

    private String songartist;

    private String songphoto;

    private String spotifylink;

    @OneToMany(mappedBy = "song",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<Favsong> favsongs;

    public void addFavsong(Favsong favsong){
        if(favsongs == null){
            favsongs = new ArrayList<>();
        }
        favsongs.add(favsong);
        favsong.setSong(this);
    }
}
