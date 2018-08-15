package fr.mistercraft.drone.commander;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class socketService extends Service {
    public static final String HOST = "192.168.1.1";
    public static final int PORT = 8888;
    private String lastmsg = "";
    private DatagramSocket connection;
    private final IBinder mBinder = new LocalBinder();

    protected class LocalBinder extends Binder {
        socketService getService() {
            return socketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void send(String message) {
        if (!message.equals(lastmsg)) {
            lastmsg = message;
            try {
                connection.send(new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(HOST), PORT));
                Log.i("Sending", message);
            } catch (IOException e) {
                Log.e("IOException", "", e);
            }
        }
    }

    public void socketConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = new DatagramSocket();
                    connection.connect(InetAddress.getByName(HOST), PORT);
                    Log.i("SocketService", "connect");
                    send("start");
                    Intent intent = new Intent(socketService.this, controlActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (IOException e) {
                    Log.e("e", "IOErreur", e);
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("e", "Erreur ", e);
                }
            }
        }).start();

    }

    public void stopCam(){
        send("stop");
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            stopCam();
            connection.disconnect();
            connection.close();
        } catch (Exception ignored) {
        }
        Log.i("SocketService", "Socket closed");
    }
}