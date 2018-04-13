package com.sanilk.securedroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

public class AlarmService extends Service {
    Handler handler;
    Handler uiHandler;

    final Context context=this;

    private final String URL="http://10.0.2.2:8080/MainServlet";

    public AlarmService() {
        handler=new Handler(Looper.getMainLooper());
        uiHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Intent intent=new Intent(context, AlarmActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Context c=context;
                while(true) {
                    try {
                        Thread.sleep(5000);

                        String address = URL;
                        URL url = new URL(address);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.connect();

                        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                        dos.writeUTF("Hello, World");
//                    connection.getOutputStream().write("Hello, world".getBytes());

                        InputStream in = connection.getInputStream();
                        String output = "";
                        int ch;
                        while ((ch = in.read()) != -1) {
                            output += (char) ch;
                        }
                        System.out.println(output);

                        Message msg=new Message();
                        msg.obj=null;
                        uiHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
        return START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
