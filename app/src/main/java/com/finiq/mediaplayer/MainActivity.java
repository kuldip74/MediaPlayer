package com.finiq.mediaplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ADMIN on 1/8/2018.
 */

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    //Menu
    Menu menu;
    private String mActivityTitle;
    //navigation drawer variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //song list variables
    private ArrayList<SongInfo> songList;
    private ListView songView;

    //service
    private MusicService musicSrv;
    private Intent playIntent;

    //binding
    private boolean musicBound = false;

    //connect to the service
    ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;

            //get service
            musicSrv = binder.getService();

            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            musicBound = false;
        }
    };
    //controller
    private MusicController controller;
    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.setting:
                        Toast.makeText(getApplicationContext(), "Setting clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.log:
                        Toast.makeText(getApplicationContext(), "Logout clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


        //retrieve list view
        songView = (ListView) findViewById(R.id.list_song);

        //instantiate list
        songList = new ArrayList<SongInfo>();

        //get song from list
        getSongList();


        mActivityTitle = getTitle().toString();

        //sort alpbabetically
        Collections.sort(songList, new Comparator<SongInfo>() {
            public int compare(SongInfo a, SongInfo b) {
                return a.getSongName().compareTo(b.getSongName());
            }
        });

        //create and set adapter
        SongAdapter songAdapter = new SongAdapter(this, songList);
        songView.setAdapter(songAdapter);

        //setup controller
        setController();
    }


    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    //user song select
    public void songPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));

        musicSrv.playSong();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem menuItem) {

        if (mToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }

        //menu item selected
        switch (menuItem.getItemId()) {
            case R.id.action_shuffle:
                    musicSrv.setShuffle();
                break;

            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //method to retrieve song info from device
    public void getSongList() {

        //query external audio
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //iterate over results
        if (cursor != null && cursor.moveToFirst()) {

            //get columns
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            //add songs to list
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                String thisAlbum = cursor.getString(albumColumn);
                int thisDuration = cursor.getInt(durationColumn);
                //Int to time convesion
                thisDuration = thisDuration / 1000;
                String startTime = "00:00";
                int minutes = thisDuration;
                int h = minutes / 60 + Integer.parseInt(startTime.substring(0, 1));
                int m = minutes % 60 + Integer.parseInt(startTime.substring(3, 4));
                String newtime = h + ":" + m;

                songList.add(new SongInfo(thisTitle, thisArtist, thisAlbum, newtime, thisId));
            } while (cursor.moveToNext());
        }

    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    //set the controller up
    private void setController() {
        controller = new MusicController(this);

        //set previous and next button listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer((MediaController.MediaPlayerControl) this);
        controller.setAnchorView(findViewById(R.id.list_song));
        controller.setEnabled(true);
    }


    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

/*    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting:
                Toast.makeText(this,"Setting clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.log:
                Toast.makeText(this,"Logout clicked",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }*/
}

