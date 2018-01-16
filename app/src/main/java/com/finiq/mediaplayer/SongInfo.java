package com.finiq.mediaplayer;

/**
 * Created by ADMIN on 1/8/2018.
 */

public class SongInfo {
    private String songName;
    private String artistName;
    private String albumName;
    private String songDuration;
    private long songID;

    public SongInfo(){
    }

    public SongInfo(String songName, String artistName, String albumName, String songDuration, long songID) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.songID = songID;
        this.songDuration = songDuration;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
