package com.apollotune.server.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavSongResponse {
    private int songId;

    private String songname;

    private String songartist;

    private String songphoto;

    private String spotifylink;
}
