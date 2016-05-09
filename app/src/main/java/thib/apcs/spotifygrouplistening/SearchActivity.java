package thib.apcs.spotifygrouplistening;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;

public class SearchActivity extends AppCompatActivity {
    public Player musicPlayer;
    SpotifyAPIHandler apiHandler = new SpotifyAPIHandler();
    Song[] songs;

    //Server
    Firebase fbServer;
    String myGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        songs = new Song[20];
        Firebase.setAndroidContext(this);
        fbServer = new Firebase("https://resplendent-inferno-2383.firebaseio.com/");
        musicPlayer = StorageManager.getPlayer();
        myGroup = StorageManager.getGroup();
        setTitle("Cassette - " + myGroup);

    }

    public void onSearchClicked(View view) {
        EditText input = (EditText) findViewById(R.id.searchField);
        String searchTerm = input.getText().toString();
        apiHandler.search(searchTerm, songs);
    }

    public void playSong(View view) {
        if (songs[0] != null) {
            String playableURI = songs[0].getPlayableURI();
            fbServer.child("groups/"+myGroup+"/playableURI").setValue(playableURI);
        }
    }
}
