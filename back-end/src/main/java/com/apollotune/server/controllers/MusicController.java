package com.apollotune.server.controllers;

import com.apollotune.server.entities.Favsong;
import com.apollotune.server.entities.Song;
import com.apollotune.server.payloads.request.DeleteFavSong;
import com.apollotune.server.payloads.request.FavsongRequest;
import com.apollotune.server.payloads.request.SongRequest;
import com.apollotune.server.payloads.response.ApiResponse;
import com.apollotune.server.payloads.response.FavSongResponse;
import com.apollotune.server.payloads.response.SongResponse;
import com.apollotune.server.security.JwtService;
import com.apollotune.server.services.FavsongService;
import com.apollotune.server.services.SongService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/song")
@RequiredArgsConstructor
public class MusicController {
    private final JwtService jwtService;
    private final SongService songService;
    private final FavsongService favsongService;
    public String getUserNameFromToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                return userNameFromDecodedToken(token);
            }
        }
        return null;
    }

    public String userNameFromDecodedToken(String token) {
        String userName = jwtService.extractUsername(token);
        return userName;
    }

    @PostMapping("/addfavoritesong")
    public SongResponse addFavSong(@RequestBody SongRequest songRequest){
        String userEmail = getUserNameFromToken();
        try {
            Song song = songService.getSongByName(songRequest.getSongname());
            favsongService.addFavsong(new FavsongRequest(song.getId(),userEmail));
            return SongResponse.builder()
                    .songartist(song.getSongartist())
                    .songname(song.getSongname())
                    .songphoto(song.getSongphoto())
                    .spotifylink(song.getSpotifylink())
                    .build();
        }catch (Exception e){
            songService.addSong(SongRequest.builder()
                            .songartist(songRequest.getSongartist())
                            .songname(songRequest.getSongname())
                            .songphoto(songRequest.getSongphoto())
                            .spotifylink(songRequest.getSpotifylink()).build());
            Song song = songService.getSongByName(songRequest.getSongname());
            favsongService.addFavsong(new FavsongRequest(song.getId(),userEmail));
            return SongResponse.builder()
                    .songartist(song.getSongartist())
                    .songname(song.getSongname())
                    .songphoto(song.getSongphoto())
                    .spotifylink(song.getSpotifylink())
                    .build();
        }
    }
    @GetMapping("/getfavoritesongs")
    public List<FavSongResponse> getAllSongsFromUser(){
        String userEmail = getUserNameFromToken();
        List<Favsong> favsongs = favsongService.getFavsongOfUser(userEmail);
        List<FavSongResponse> songs = new ArrayList<>();
        for(Favsong favsong : favsongs){
            songs.add(FavSongResponse.builder()
                    .songId(favsong.getSong().getId())
                    .songartist(favsong.getSong().getSongartist())
                    .songname(favsong.getSong().getSongname())
                    .songphoto(favsong.getSong().getSongphoto())
                    .spotifylink(favsong.getSong().getSpotifylink())
                    .build());
        }
        return songs;
    }
    @DeleteMapping("/deletefavoritesong")
    public ApiResponse deleteFavSongFromUser(@RequestBody DeleteFavSong deleteFavSong){
        String userEmail = getUserNameFromToken();
        return favsongService.deleteFavsong(FavsongRequest.builder()
                .userEmail(userEmail)
                .songId(deleteFavSong.getFavSongId())
                .build());
    }
}
