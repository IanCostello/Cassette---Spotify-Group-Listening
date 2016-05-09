package thib.apcs.spotifygrouplistening;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

    public void updatePlayer(final View view, final String spotifyURI) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //Get Spotify URI
                    String uri = spotifyURI.substring(spotifyURI.indexOf("k:")+1, spotifyURI.length());

                    //Init Bytebuffer
                    ByteBuffer searchUrl = new ByteBuffer();

                    //Make an GET request to spotify
                    searchUrl.readURL(new URL(uri));

                    //Get initial JSON Object
                    JSONTokener all = new JSONTokener(searchUrl.toString());

                    //Get the track
                    JSONObject all2 = (JSONObject) all.nextValue();
                    JSONObject track = all2.getJSONObject("album");

                    //Get Necessary Objects
                    final String title = track.getString("name");
                    final String artistName = track.getJSONArray("artists").getJSONObject(0).getString("name");
                    final String albumUrl = track.getJSONArray("images").getString(2);

                    //Load the album cover
                    final Bitmap bitmap = loadImage(albumUrl);

                    //Update Everything on UI Thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView cover = (ImageView) view.findViewById(R.id.albumCover);
                            cover.setImageBitmap(bitmap);

                            TextView titleView = (TextView) view.findViewById(R.id.song);
                            titleView.setText(title);

                            TextView artistView = (TextView) view.findViewById(R.id.artist);
                            titleView.setText(artistName);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Bitmap loadImage(String urlSrc) {
        try {
            //Establish URL Connection
            URL url = new URL(urlSrc);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            //Ensure Good Connection
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return null;
            }

            //Read The Stream
            InputStream in = null;
            in = urlConnection.getInputStream();

            //Decode the Stream
            Bitmap myBitmap = BitmapFactory.decodeStream(in);

            //Return
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void search(String[] terms, Song[] songs) {
        Thread thread = new Search(terms, songs);
        thread.start();
    }

    /** search
     * Aux function that allows for a raw string to be inputted and separates each work
     * @param s
     */
    public void search(String s, Song[] songs) {
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
        search(terms, songs);
    }

    public class Search extends Thread {
        String[] terms;
        Song[] songs;

        public Search (String[] terms, Song[] songs) {
            this.terms = terms;
            this.songs = songs;
        }

        public void run() {
            //Suppress Main Thread Networking Exception
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //Init Local Vars
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
                JSONTokener all = new JSONTokener(searchUrl.toString());

                //Get track overarching list
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

                    //Get Album Cover
                    //String url = track.getJSONArray("images").getString(2);
                    String url = "";

                    //Get Spotify URI
                    String id = track.getString("id");
                    songs[i] = (new Song(id, trackName, artistName, url));
                }

                System.out.println("DONE LOADING FROM NETWORK");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
