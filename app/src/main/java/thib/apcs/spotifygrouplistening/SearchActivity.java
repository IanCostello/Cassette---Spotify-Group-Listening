package thib.apcs.spotifygrouplistening;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;

public class SearchActivity extends AppCompatActivity {
    public Player musicPlayer;

    //Results
    Song[] songs;
    ListView songResults;

    //Server
    FirebaseDatabase fbServer;
    String myGroup;
    SpotifyAPIHandler apiHandler = new SpotifyAPIHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Init Songs and Results
        songs = new Song[20];
        songResults = (ListView) findViewById(R.id.searchListView);
        songResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playSong(position);
            }
        });

        //Init Firebase Server
        fbServer = FirebaseDatabase.getInstance();

        //Get Music Player and Group
        musicPlayer = StorageManager.getPlayer();
        myGroup = StorageManager.getGroup();
        setTitle("Cassette - " + myGroup);

    }

    /** onSearchClicked
     * Begin Search for Term
     * @param view
     */
    public void onSearchClicked(View view) {
        //Get Search Field
        EditText input = (EditText) findViewById(R.id.searchField);
        String searchTerm = input.getText().toString();

        //Pass the search terms onto api handler
        apiHandler.search(getApplicationContext(), getWindow().getDecorView().getRootView(), searchTerm, songs);
    }

    /** Plays Song at Given Index*/
    public void playSong(int index) {
        //Get the song
        Song song = songs[index];

        //Ensure valid then update server
        if (song != null) {
            String playableURI = song.getPlayableURI();
            DatabaseReference songRef = fbServer.getReference("groups/"+myGroup+"/playableURI");
            songRef.setValue(playableURI);
        }
    }
}
