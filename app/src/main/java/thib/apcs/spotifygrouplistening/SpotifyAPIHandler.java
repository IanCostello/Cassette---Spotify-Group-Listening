package thib.apcs.spotifygrouplistening;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    public void run() {

    }

    public void search(String[] terms) {
        Thread thread = new Search(terms);
        thread.start();
    }

    /** search
     * Aux function that allows for a raw string to be inputted and separates each work
     * @param s
     */
    public void search(String s) {
        //Get Num Spaces to Init Array to right size
        int spaces = 0;
        for (int i = 0; i < s.length(); i+=1) {
            if (s.charAt(i) == ' ')
                spaces+=1;
        }

        //Init Array To Correct Size
        String[] terms = new String[spaces+1];
        int start = 0;
        int count = 0;

        //Split array into words
        for (int i = 0; i < s.length(); i+=1) {
            if (s.charAt(i) == ' ') {
                terms[count] = s.substring(start, i);
                i+=1;
                start = i;
                count+=1;
            }
        }

        //Add final word
        terms[count] = s.substring(start, s.length());

        //Pass on terms
        search(terms);
    }

    public class Search extends Thread {
        String[] terms;

        public Search (String[] terms) {
            this.terms = terms;
        }

        public void run() {
            //Suppress Main Thread Networking Exception
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //Init Local Vars
            ArrayList<Song> songs = new ArrayList<Song>();
            ByteBuffer searchUrl = new ByteBuffer();

            //Init Search URL with basic request info
            String urlToSearch = searchString;

            //Add each search term with spaces indicated by '+'
            for (String s: terms) {
                urlToSearch += s + "+";
            }

            //Specifies that only tracks should be returned
            urlToSearch += "&type=track"; //TODO Should return artists?

            try {
                //Make an GET request to spotify
                searchUrl.readURL(new URL(urlToSearch));

                //Get initial JSON Object
                //JSONObject all = new JSONObject(searchUrl.toString());
                JSONTokener all = new JSONTokener(searchUrl.toString());

                //Get track overarching list
                //JSONObject tracks = new JSONObject("tracks");
                JSONObject all2 = (JSONObject) all.nextValue();
                JSONObject tracks = all2.getJSONObject("tracks");

                //Get JSON array, which contains each search term in a list
                JSONArray allTracks = tracks.getJSONArray("items");
                //   JSONArray allTracks = new JSONArray(searchUrl.toString());

                //Loop Through Each Track
                for (int i=0; i < allTracks.length(); i+=1) {
                    //Get Track
                    JSONObject track = allTracks.getJSONObject(i);
                    String trackName = track.getString("name");

                    //Get Artist
                    JSONObject artist = track.getJSONArray("artists").getJSONObject(0);
                    String artistName = artist.getString("name");

                    //Get Spotify URI
                    String id = track.getString("id");
                    songs.add(new Song(id, trackName, artistName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Praint each returned song to console
            //TODO actually populate an array with this info
            for (Song song: songs) {
                System.out.println();
            }
        }
    }

}
