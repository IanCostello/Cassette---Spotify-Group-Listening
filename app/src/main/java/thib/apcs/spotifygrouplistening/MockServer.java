package thib.apcs.spotifygrouplistening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iancostello on 5/5/16.
 */
public class MockServer {
    private static final String[] testSongs = {"",""};
    private ArrayList<String> songs;
    private Map<String, Group> groups;

    public MockServer () {
        //Init Mock Song List
        songs = new ArrayList<String>();
        addAllSongsBack();

        //Init Groups
        groups = new HashMap<>();
        //Add fake groups
        Group testGroup = new Group("Menlo");
        groups.put("Menlo", testGroup);
    }

    public boolean authenticate(String groupID, String groupPassword) {
        return groups.get(groupID).authenticate(groupPassword);
    }

    public String getCurrentSongURI() {
        if (songs.size() == 0)
            addAllSongsBack();
        return songs.remove((int) (Math.random() * testSongs.length));
    }

    private void addAllSongsBack() {
        for (String s: testSongs)
            songs.add(s);
    }
}
