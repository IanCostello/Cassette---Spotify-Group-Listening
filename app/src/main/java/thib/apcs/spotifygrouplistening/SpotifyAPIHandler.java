package thib.apcs.spotifygrouplistening;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import me.iancostello.util.ByteBuffer;

/**
 * Created by iancostello on 5/5/16.
 *
 */
public class SpotifyAPIHandler {
    private static final String searchString = "https://api.spotify.com/v1/search?q=";

    public void search(String[] terms) {
        ArrayList<Song> songs = new ArrayList<Song>();

        ByteBuffer searchUrl = new ByteBuffer();
        String urlToSearch = searchString;
        for (String s: terms) {
            urlToSearch += s + "+";
        }
        urlToSearch += "&type=track"; //TODO Should return artists?
        try {
            searchUrl.readURL(new URL(urlToSearch));
            JSONArray results = new JSONArray(searchUrl.toString());

            for (int i=0; i < results.length(); i+=1) {
                JSONObject track = results.getJSONObject(i);
                String trackName = track.getString("name");
                JSONObject artist = track.getJSONArray("artists").getJSONObject(0);
                String artistName = artist.getString("name");
                String id = track.getString("id");
                songs.add(new Song(id, trackName, trackName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Song song: songs) {
            System.out.println();
        }

    }

    public void search(String s) {
        int spaces = 0;
        //Get num spaces
        for (int i = 0; i < s.length(); i+=1) {
            if (s.charAt(i) == ' ')
                spaces+=1;
        }

        String[] terms = new String[spaces];
        int start = 0;
        int count = 0;

        for (int i = 0; i < s.length(); i+=1) {
            if (s.charAt(i) == ' ') {
                terms[count] = s.substring(start, i);
                i+=1;
                start = i;
                count+=1;
            }
        }

        search(terms);
    }

}
