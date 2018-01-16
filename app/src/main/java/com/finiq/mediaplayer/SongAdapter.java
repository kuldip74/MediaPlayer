package com.finiq.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ADMIN on 1/8/2018.
 */

public class SongAdapter extends BaseAdapter {

    //song list and layout
    private ArrayList<SongInfo> songsList;
    private LayoutInflater songInf;

    //constructor
    public SongAdapter(Context c, ArrayList<SongInfo> theSongs){
        songsList = theSongs;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("colorDarkGray"));
        } else {
            view.setBackgroundColor(Color.parseColor("colorGray"));
        }
*/

        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate(R.layout.song_list_item, parent, false);
        //get title artist and album views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        TextView durationView = (TextView) songLay.findViewById(R.id.song_duration);
        //get song using position
        SongInfo currSong = songsList.get(position);
        //get title, artist and album strings
        songView.setText(currSong.getSongName());
        artistView.setText(currSong.getArtistName());
        durationView.setText(currSong.getSongDuration());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}
