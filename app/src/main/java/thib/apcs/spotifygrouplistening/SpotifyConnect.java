package thib.apcs.spotifygrouplistening;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

public class SpotifyConnect extends AppCompatActivity  implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // Client ID
    private static final String CLIENT_ID = "14f0ca37d51b43fa8cdc17e1a037c026";
    // Redirect URI
    private static final String REDIRECT_URI = "spotify-group-listening-login://callback";
    // Verify integrity of request
    private static final int LOGIN_REQUEST_CODE = 3013;
    //Spotify User
    private Player musicPlayer;
    //Mock Server
    MockServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Load the interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_connect);
        server = new MockServer();

    }

    /** loginOnClick
     * Created by
     * @param view
     */
    public void loginOnClick(View view ) {
        //Create the Authentication Request Builder
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"}); //Just allows for streaming
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request);
    }

    public void playNextSong(View view) {
        musicPlayer.play(server.getCurrentSongURI());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == LOGIN_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config musicPlayerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(musicPlayerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        musicPlayer = player;
                        musicPlayer.addConnectionStateCallback(SpotifyConnect.this);
                        musicPlayer.addPlayerNotificationCallback(SpotifyConnect.this);
                        
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("Connect Activity", "Player not initialized: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Oops! Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}