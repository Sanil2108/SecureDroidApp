package com.sanilk.securedroid;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainNetworkingService extends Service {
    private final static int MILLISECONDS=3000;
    private final static String URL="10.0.2.2:8080/MainServlet";

    public MainNetworkingService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_main))
                .setTicker(getString(R.string.notification_main))
                .setContentText(getString(R.string.notification_sub))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build();
        startForeground(123, notification);

        final Context context=this;

        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true) {
                        Thread.sleep(MILLISECONDS);

                        URL url=new URL(URL);
                        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        DataOutputStream dos=new DataOutputStream(
                                connection.getOutputStream()
                        );
                        dos.writeUTF("i need some info, plsss");

                        DataInputStream dis=new DataInputStream(
                                connection.getInputStream()
                        );
                        String response=dis.readUTF();


//                        Intent intent=new Intent(context, AlarmActivity.class);
//                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
