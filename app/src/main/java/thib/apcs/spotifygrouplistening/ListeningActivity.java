package thib.apcs.spotifygrouplistening;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.spotify.sdk.android.player.Player;

public class ListeningActivity extends AppCompatActivity {
    //Firebase Server
    Firebase fbServer;
    //Group
    private String group;
    //Music Player
    Player mPlayer;
    private String currentSong;
    //Play Button
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //Get Group
        group = StorageManager.getGroup();

        setTitle("Cassette - " + group);

        //Get Player
        mPlayer = StorageManager.getPlayer();

        //Init Firebase
        Firebase.setAndroidContext(this);
        fbServer = new Firebase("https://resplendent-inferno-2383.firebaseio.com/");
        fbServer.child("groups/"+group+"/playableURI").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String songURI = (String) snapshot.getValue();
                updatePlayer(songURI);
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
    }

    public void updatePlayer(String songURI) {
        if (songURI != null) {
            mPlayer.play(songURI);
           // SpotifyAPIHandler.updatePlayer(getWindow().getDecorView().getRootView(), songURI);
        }
    }

    public void playButtonClicked(View view) {
        ImageView playButton = (ImageView) findViewById(R.id.playButton);
        if (isPlaying) {
            playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_48dp);
            mPlayer.pause();
            isPlaying = false;
        } else {
            playButton.setImageResource(R.drawable.playbutton);
            mPlayer.resume();
            isPlaying = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.listening_activity, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
