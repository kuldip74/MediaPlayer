
package com.finiq.mediaplayer.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveFavouriteResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Artist")
    @Expose
    private String artist;
    @SerializedName("Album")
    @Expose
    private String album;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

}
