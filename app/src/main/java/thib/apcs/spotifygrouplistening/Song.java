package thib.apcs.spotifygrouplistening;

/**
 * Created by iancostello on 5/5/16.
 */
public class Song {
    private String songURI;
    private String songName;
    private String artist;

    public Song(String songURI, String songName, String artist) {
        this.songURI = songURI;
        this.songName = songName;
        this.artist = artist;
    }

    public String toString() {
        return songName + " by " + artist;
    }

    public String getSongURI() { return songURI; }
    public String getSongName() { return songName; }
    public String getArtist() { return artist; }

}
