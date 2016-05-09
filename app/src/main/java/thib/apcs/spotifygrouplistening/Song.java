package thib.apcs.spotifygrouplistening;

/**
 * Created by iancostello on 5/5/16.
 */
public class Song {
    private String songID;
    private String songName;
    private String artist;
    private String albumCover;

    public static final String API_ACCESS_STRING = "spotify:track:";

    public Song(String songID, String songName, String artist, String albumCover) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
    }

    public String toString() {
        return songName + " by " + artist;
    }

    public String getPlayableURI() { return API_ACCESS_STRING + songID; }
    public String getSongURI() { return songID; }
    public String getSongName() { return songID; }
    public String getArtist() { return artist; }
    public String getAlbumCover() { return albumCover; }

}
