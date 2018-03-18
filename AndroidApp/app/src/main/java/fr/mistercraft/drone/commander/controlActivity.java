package fr.mistercraft.drone.commander;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class controlActivity extends AppCompatActivity {
    boolean mBound = false;
    private socketService socketservice;
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
        setContentView(R.layout.activity_control);
    }
}
