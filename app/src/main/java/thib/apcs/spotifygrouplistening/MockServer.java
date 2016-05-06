package thib.apcs.spotifygrouplistening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iancostello on 5/5/16.
 */
public class MockServer {
    private Map<String, Group> groups;

    public MockServer () {
        //Init Groups
        groups = new HashMap<>();

        //Add fake groups
        Group testGroup = new Group("Menlo");
        groups.put("Menlo", testGroup);
    }

    /** authenticate
     * Dummy Authenticate Method
     */
    public boolean authenticate(String groupID, String groupPassword) {
        return groups.get(groupID).authenticate(groupPassword);
    }

    /** getGroup
     * Dummy getGroup Method
     * @return
     */
    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }
}
