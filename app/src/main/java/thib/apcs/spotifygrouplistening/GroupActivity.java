package thib.apcs.spotifygrouplistening;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.iancostello.util.ByteBuffer;

public class GroupActivity extends AppCompatActivity {
    FirebaseDatabase fbServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbServer = FirebaseDatabase.getInstance();
    }

    public void onCreateGroup(View view) {
        final String group = ((EditText) findViewById(R.id.groupName)).getText().toString();
        String groupHash = "";

        final String toQuery = group;
        final DatabaseReference groups = fbServer.getReference("groups");

        groups.child(toQuery).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { //TODO change this to existing groups
                    StorageManager.setGroup(toQuery);
                    groups.child(toQuery+"/groupName").setValue(group);
                    Intent intent2 = new Intent(GroupActivity.this, ListeningActivity.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(GroupActivity.this, "Group Already Exists!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public void onConnect(View view) {
        String group = ((EditText) findViewById(R.id.groupName)).getText().toString();
        String groupHash = "";

        final String toQuery = group;
        final DatabaseReference groups = fbServer.getReference("groups");

        groups.child(toQuery).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StorageManager.setGroup(toQuery);

                    Intent intent2 = new Intent(GroupActivity.this, ListeningActivity.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(GroupActivity.this, "Incorrect Password or nonexistent group!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    private static String hashString(String toEncrypt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            ByteBuffer buf = new ByteBuffer();
            buf.appendHex(digest.digest(toEncrypt.getBytes("UTF-8")));
            return buf.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
