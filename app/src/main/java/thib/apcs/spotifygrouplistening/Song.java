package thib.apcs.spotifygrouplistening;

import android.graphics.Bitmap;

/**
 * Created by iancostello on 5/5/16.
 */
public class Song {
    private String songID;
    private String songName;
    private String artist;
    private Bitmap albumCover;

    public static final String API_ACCESS_STRING = "spotify:track:";

    public Song(String songID, String songName, String artist, Bitmap albumCover) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
        this.albumCover = albumCover;
    }

    public Song(String songID, String songName, String artist) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
        this.albumCover = albumCover;
    }

    public String toString() {
        return songName + " by " + artist;
    }

    public String getPlayableURI() { return API_ACCESS_STRING + songID; }
    public String getSongURI() { return songID; }
    public String getSongName() { return songID; }
    public String getArtist() { return artist; }
    public Bitmap getAlbumCover() { return albumCover; }

}
