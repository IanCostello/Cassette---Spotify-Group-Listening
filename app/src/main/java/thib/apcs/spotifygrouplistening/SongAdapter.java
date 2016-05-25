package thib.apcs.spotifygrouplistening;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by iancostello on 5/10/16.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Context context, Song[] songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the song at view
        Song song = getItem(position);

        //Create View
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.song_in_list, parent, false);
        }

        //Get TextView Objects
        TextView song2 = (TextView) convertView.findViewById(R.id.song);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);

        //Set Song and Artist/Album
        song2.setText(song.getSongName());
        artist.setText(song.getArtist() + " * " + song.getAlbum());

        return convertView;
    }
}

