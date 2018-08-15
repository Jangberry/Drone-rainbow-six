package fr.mistercraft.drone.commander;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import yjkim.mjpegviewer.MjpegView;

public class controlActivity extends AppCompatActivity {
    private boolean mBound = false;
    private socketService socketservice;
    private static final int StreamPORT = 8889;
    private MjpegView video;
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    private final ServiceConnection serviceconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            socketService.LocalBinder binder = (socketService.LocalBinder) service;
            socketservice = binder.getService();
            mBound = true;
            JoystickView joystick = findViewById(R.id.joystick);
            joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
                @Override
                public void onMove(int angle, int strength) {
                    String result;
                    if (angle >= 0 && angle < 90) {
                        result = String.valueOf(((int) ((angle - 45.0) * 2.83 * strength / 100)) + 128);  // droite
                        result += " ";
                        result += String.valueOf(((int) (127.0 * strength / 100)) + 128);  // gauche
                    } else if (angle >= 90 && angle < 180) {
                        result = String.valueOf(((int) (127.0 * strength / 100)) + 128);  // droite
                        result += " ";
                        result += String.valueOf(((int) (-(angle - 135.0) * 2.83 * strength / 100)) + 128);  // gauche
                    } else if (angle >= 180 && angle < 270) {
                        result = String.valueOf(((int) (-(angle - 225.0) * 2.83 * strength / 100)) + 128);  // droite
                        result += " ";
                        result += String.valueOf(((int) (-127.0 * strength / 100)) + 128);  // gauche
                    } else if (angle >= 270 && angle < 360) {
                        result = String.valueOf(((int) (-127.0 * strength / 100)) + 128);  // droite
                        result += " ";
                        result += String.valueOf(((int) ((angle - 315.0) * 2.83 * strength / 100)) + 128);  // gauche
                    } else {
                        result = "128 128";
                    }
                    socketservice.send(result);
                }
            }, 100);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Intent serviceintent = new Intent(this, socketService.class);
        bindService(serviceintent, serviceconnection, Context.BIND_AUTO_CREATE);
        video = findViewById(R.id.video);
        video.Start("HTTP://" + socketService.HOST + ":" + StreamPORT + "/stream/video.mjpeg", new Handler() {
            @Override
            public void handleMessage(Message msg){
                Log.d("State : ", msg.obj.toString());

                switch (msg.obj.toString()){
                    case "DISCONNECTED" :
                        Log.v("Stream", "Disconnected");
                        break;
                    case "CONNECTION_PROGRESS" :
                        Log.v("Stream", "Connection in progress");
                        break;
                    case "CONNECTED" :
                        Log.v("Stream", "Connected");
                        break;
                    case "CONNECTION_ERROR" :
                        Log.v("Stream", "Connection error");
                        break;
                    case "STOPPING_PROGRESS" :
                        Log.v("Stream", "Stopped");
                        break;
                }

            }
        });
        video.SetDisplayMode(MjpegView.SIZE_FIT);
    }

    protected void onDestroy() {
        super.onDestroy();
        socketservice.stopCam();
        video.Stop();
        unbindService(serviceconnection);
        mBound = false;
    }

    public void onReturn(View view) {
        startActivity(new Intent(this, connectionScreen.class));
        Log.v("ControlActivity", "Launched main activity");
        finish();
    }
}
