package fr.mistercraft.drone.commander;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class connectionScreen extends AppCompatActivity {

    private socketService socketservice;
    boolean mBound = false;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    protected ServiceConnection serviceconnection = new ServiceConnection() {

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

    protected void onStart() {
        super.onStart();
        Intent serviceintent = new Intent(this, socketService.class);
        bindService(serviceintent, serviceconnection, Context.BIND_AUTO_CREATE);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceconnection);
        mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_screen);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        SharedPreferences Saved_IP = this.getSharedPreferences("IP", MODE_PRIVATE);
        String saved_IP = Saved_IP.getString("IP", null);
        TextView IP = findViewById(R.id.IP);
        IP.setText(saved_IP);
        setTitle(R.string.maintitle);
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
public void test(){
        findViewById(R.id.button).setVisibility(View.GONE);
}
    public void onConnect(View view) {
        TextView IP = findViewById(R.id.IP);
        String ip = IP.getText().toString();
        SharedPreferences Saved_IP = this.getSharedPreferences("IP", MODE_PRIVATE);
        SharedPreferences.Editor Save_IP = Saved_IP.edit();
        Save_IP.putString("IP", ip);
        Save_IP.apply();
        socketservice.socketConnect(ip);
    }
}
