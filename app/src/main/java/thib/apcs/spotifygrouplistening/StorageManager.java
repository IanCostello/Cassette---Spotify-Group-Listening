package thib.apcs.spotifygrouplistening;

import com.spotify.sdk.android.player.Player;

/**
 * Created by iancostello on 5/8/16.
 */
public class StorageManager {
    private static Player mainPlayer;
    private static String myGroup = "Menlo"; //TODO implement properly

    /** Getters and Setters */
    public static void setPlayer(Player musicPlayer) {
        mainPlayer = musicPlayer;
    }
    public static Player getPlayer() {
        return mainPlayer;
    }
    public static void setGroup(String s) { myGroup = s; }
    public static String getGroup() { return myGroup; }
}
