package fr.mistercraft.drone.commander;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.VideoView;

public class connectionScreen extends AppCompatActivity {

    private socketService socketservice;
    private boolean mBound = false;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    private final ServiceConnection serviceconnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            socketService.LocalBinder binder = (socketService.LocalBinder) service;
            socketservice = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceconnection);
        mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceintent = new Intent(this, socketService.class);
        startService(serviceintent);
        bindService(serviceintent, serviceconnection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_connection_screen);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        setTitle(R.string.maintitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.untitled));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_aboutButton:
                Intent aboutActivityCall = new Intent(this, AboutTab.class);
                startActivity(aboutActivityCall);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onConnect(View view) {
        socketservice.socketConnect();
        finish();
    }
}
