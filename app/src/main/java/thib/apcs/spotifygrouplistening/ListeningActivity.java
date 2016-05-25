package thib.apcs.spotifygrouplistening;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.player.Player;

public class ListeningActivity extends AppCompatActivity {
    //Firebase Server
    FirebaseDatabase fbServer;
    //Group
    private String group;
    //Music Player
    Player mPlayer;
    private String currentSong;
    //Play Button
    private boolean isPlaying = true;
    //Progress Bar
    ProgressBar songProgress;

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
        fbServer = FirebaseDatabase.getInstance();
        DatabaseReference myGroup = fbServer.getReference("groups/"+group);

        //Add Event Listener
        myGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String songURI = (String) snapshot.child("/playableURI").getValue();
                updatePlayer(songURI);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Firebase", "Cancelled");
            }
        });


        new UpdateProgressBar().start();
    }

    /** UpdateProgressBar
     * Updates the song progress bar
     */
    public class UpdateProgressBar extends Thread {
        @Override
        public void run() {
            final ProgressBar songProgress = (ProgressBar) findViewById(R.id.songProgress);
            while (true) {
                try {
                    //If it is playing increase
                    if (isPlaying) {
                        songProgress.setProgress(songProgress.getProgress() + 50);
                        Thread.sleep(50);
                    }
                } catch (Exception e) {
                }
            }
        }

    }

    /** updatePlayer
     * Plays the song and updates play button
     * @param songURI
     */
    public void updatePlayer(String songURI) {
        //Ensure not null
        if (songURI != null) {
            //Get album cover and song information and update the player
            SpotifyAPIHandler.updatePlayer(getWindow().getDecorView().getRootView(), songURI);

            //Play the song
            mPlayer.play(songURI);

            //Reset progress
            ((ProgressBar) findViewById(R.id.songProgress)).setProgress(0);

            //update player if paused
            if(!isPlaying) {
                ImageView playButton = (ImageView) findViewById(R.id.playButton);
                playButton.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                isPlaying = true;
            }
        }
    }

    /** playButtonClicked
     * Switches the play button
     * @param view
     */
    public void playButtonClicked(View view) {
        //Get the imageview
        ImageView playButton = (ImageView) findViewById(R.id.playButton);

        //Switch the button
        if (isPlaying) {
            playButton.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
            mPlayer.pause();
            isPlaying = false;
        } else {
            playButton.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
            mPlayer.resume();
            isPlaying = true;
        }
    }

    /** onCreateOptionsMenu
     * Populates options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.listening_activity, menu);
        return true;

    }

    /** onOptionsItemSelected
     * Calls when something in the menu is called
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User selected search
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                return true;

            case R.id.action_settings:
                // Settings selected, but activity currently doesn't exist
                return true;

            default:
                //Unrecognized
                return super.onOptionsItemSelected(item);

        }
    }
}
