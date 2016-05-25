package thib.apcs.spotifygrouplistening;

import android.graphics.Bitmap;

/**
 * Created by iancostello on 5/5/16.
 */
public class Song {
    private String songID;
    private String songName;
    private String artist;
    private String album;
    private Bitmap albumCover;

    //Spotify Track URI Prefix
    public static final String API_ACCESS_STRING = "spotify:track:";

    /** Constructor With Album Image For Player */
    public Song(String songID, String songName, String artist, Bitmap albumCover) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
        this.albumCover = albumCover;
    }

    /** Constructor With Album Name For Search Results*/
    public Song(String songID, String songName, String artist, String album) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
        this.album = album;
    }

    /** Getters and Setters*/
    public String toString() { return songName + " by " + artist; }
    public String getPlayableURI() { return API_ACCESS_STRING + songID; }
    public String getSongURI() { return songID; }
    public String getSongName() { return songName; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public Bitmap getAlbumCover() { return albumCover; }

}
