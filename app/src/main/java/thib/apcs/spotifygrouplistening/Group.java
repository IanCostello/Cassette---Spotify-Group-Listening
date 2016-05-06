package thib.apcs.spotifygrouplistening;

/**
 * Created by iancostello on 5/5/16.
 */
public class Group {
    private String groupID;
    private String password; //TODO SHA HASH
    private String currSong;


    public Group(String groupID) {
        this.groupID = groupID;
        password = null;
    }

    public boolean authenticate() {
        return authenticate("");
    }

    public boolean authenticate(String s) {
        if (password == null) {
            return true;
        }
        return s.equals(password);
    }
}
