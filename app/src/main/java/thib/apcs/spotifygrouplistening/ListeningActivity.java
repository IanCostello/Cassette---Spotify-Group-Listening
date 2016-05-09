package thib.apcs.spotifygrouplistening;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //Get Group
       group = StorageManager.getGroup();

        //Get Player
        mPlayer = StorageManager.getPlayer();

        //Init Firebase
        Firebase.setAndroidContext(this);
        fbServer = new Firebase("https://resplendent-inferno-2383.firebaseio.com/");
        fbServer.child("groups/"+group+"/playableURI").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String songURI = (String) snapshot.getValue();
                if (songURI != null) {
                    mPlayer.play((String) snapshot.getValue());

                }
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
    }
}
