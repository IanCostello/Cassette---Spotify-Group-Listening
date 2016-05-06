package thib.apcs.spotifygrouplistening;

import java.util.ArrayList;

/**
 * Created by iancostello on 5/5/16.
 */
public class Group {
    private String groupID;
    private String password; //TODO SHA HASH
    private String currSong;
    private ArrayList<String> queue;
    private static final String[] testSongs = {"spotify:track:6GnhWMhgJb7uyiiPEiEkDA","spotify:track:5QwU7QaUPx0jUNwnd6h9Nb", "spotify:track:0tCuyg1OikbQ4nIq36n26D"};

    public Group(String groupID) {
        //Init Group Name
        this.groupID = groupID;
        password = null;

        //Init Mock Song List
        queue = new ArrayList<String>();
        addAllSongsBack();
    }

    /** authenticate
     * Blank authenticate
     * @return
     */
    public boolean authenticate() {
        return authenticate("");
    }

    /** authenticate
     * Authenticates user based on user and pass
     * @param s
     * @return
     */
    public boolean authenticate(String s) {
        if (password == null) {
            return true;
        }
        return s.equals(password);
    }

    /** addAllSongsBack
     * Repopulates the queue
     */
    private void addAllSongsBack() {
        for (String s: testSongs)
            queue.add(s);
    }

    /** getCurrentSongURI
     * Returns next song in queue
     */
    public String getCurrentSongURI() {
        if (queue.size() == 0)
            addAllSongsBack();
        return queue.remove((int) (Math.random() * queue.size()));
    }

    public ArrayList<String> getQueue() {
        return queue;
    }
}
