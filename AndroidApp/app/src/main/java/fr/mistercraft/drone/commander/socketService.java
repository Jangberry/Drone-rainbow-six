package fr.mistercraft.drone.commander;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class socketService extends Service {
    Socket socket;
    OutputStream out;
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


    protected class sendThread implements Runnable {
        private String message;

        private sendThread(String message) {
            this.message = message;
        }

        public void run() {
            try {
                out.write(message.getBytes());
                Log.i("Sending", "Sent : " + message);
            } catch (IOException e) {
                Log.e("IOException", "", e);
                Intent intent = new Intent(socketService.this, connectionScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("E", "Unknown error ", e);
                Intent intent = new Intent(socketService.this, connectionScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    public void send(String message) {
        new Thread(new sendThread(message)).start();
    }

    public void socketConnect(final String ip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketAddress socaddrs = new InetSocketAddress(ip, 4277);
                    socket = new Socket();
                    Log.i("SocketService", "Tying to connect");
                    socket.connect(socaddrs, 2000);
                    if (socket.isConnected()) {
                        out = socket.getOutputStream();
                        Intent intent = new Intent(socketService.this, controlActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.i("SocketService", "Socket connected");
                    }
                } catch (IOException e) {
                    Log.e("e", "IOErreur", e);
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("e", "Erreur ", e);
                }
            }
        }).start();

    }

    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            Log.e("e", "IO", e);
        } catch (Exception e) {
        }
        Log.i("SocketService", "Socket closed");
    }
}