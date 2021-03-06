package com.sanilk.securedroid;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sanilk.securedroid.entities.User;
import com.sanilk.securedroid.networking.JSONParser;
import com.sanilk.securedroid.networking.requests.SimpleRequestForQueries;
import com.sanilk.securedroid.networking.requests.SimpleRequestForQueriesInterface;
import com.sanilk.securedroid.networking.requests.UpdateLocationRequest;
import com.sanilk.securedroid.networking.responses.SimpleResponseForQueries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainNetworkingService extends Service {
    private final static int MILLISECONDS=4000;
//    public final static String URL="http://10.0.2.2:8080/MainServlet";
    public final static String URL="http://192.168.1.4:8080/MainServlet";

    private User userLoggedIn;

    private final static String TAG="MAIN_NETWORKING_SERVICE";

    //in milliseconds
    private long lastDateSMSChecked=0;
    private boolean serviceRunning;

    public MainNetworkingService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        userLoggedIn=new User(
                intent.getStringExtra("EMAIL"),
                intent.getStringExtra("PASSWORD")
        );

        serviceRunning=true;

        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_main))
                .setTicker(getString(R.string.notification_main))
                .setContentText(getString(R.string.notification_sub))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build();
        startForeground(123, notification);

        final Context context=this;

        Thread t=new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    try{
                        if(!serviceRunning){
                            stopSelf();
                            break;
                        }
                        Thread.sleep(MILLISECONDS);
//                        showAlarm();
                        sendSimpleRequestForQueries(new SimpleRequestForQueriesInterface() {
                            @Override
                            public void onComplete(SimpleResponseForQueries simpleResponseForQueries) {
                                if(simpleResponseForQueries==null){
                                    return;
                                }
                                for(SimpleResponseForQueries.Action action:simpleResponseForQueries.getActions()){
                                    if(action instanceof SimpleResponseForQueries.StartAlarmAction){
                                        Log.d(TAG, "StartAlarm action to be executed");
                                        showAlarm();
                                    }else if(action instanceof SimpleResponseForQueries.MessageAction){
                                        Log.d(TAG, "Message action to be executed");
                                        showMessage((SimpleResponseForQueries.MessageAction)action);
                                    }

                                }

                            }
                        });
                        sendUpdateLocationRequest();
//                        readSMS();

//                        Intent intent=new Intent(context, AlarmActivity.class);
//                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
            }
        });
        t.start();

        return START_NOT_STICKY;
    }

    public void showAlarm(){
        Intent intent=new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void showMessage(SimpleResponseForQueries.MessageAction messageAction){
        Intent intent=new Intent(this, MessageActivity.class);
        intent.putExtra("messageText", messageAction.getMessageText());
        startActivity(intent);
    }

    public void sendSimpleRequestForQueries(SimpleRequestForQueriesInterface simpleRequestForQueriesInterface) throws Exception{
        URL url=new URL(URL);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream dos=new DataOutputStream(
                connection.getOutputStream()
        );

        SimpleRequestForQueries simpleRequestForQueries=
                new SimpleRequestForQueries();
        simpleRequestForQueries.setClientEmail(userLoggedIn.getEmail());

        dos.writeUTF(simpleRequestForQueries.getJSONString());

        DataInputStream dis=new DataInputStream(
                connection.getInputStream()
        );
        String response=dis.readUTF();
        connection.disconnect();

        JSONParser jsonParser=new JSONParser();
        SimpleResponseForQueries simpleResponseForQueries=(SimpleResponseForQueries) jsonParser.parse(response);
        simpleRequestForQueriesInterface.onComplete(simpleResponseForQueries);

        Log.d(TAG, response);
    }

    @Override
    public void onDestroy() {
        serviceRunning=false;
    }

    //SMS Constants
    private static final String MESSAGE_TITLE="SECURE_DROID";

    private static final String WIFI_ON_COMMAND="WIFI ON";
    private static final String WIFI_OFF_COMMAND="WIFI OFF";
    public void readSMS(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "READ_SMS PERMISSION NOT GRANTED");
            return;
        }
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        ArrayList<String> messages=new ArrayList<>();
        long temp=-1;
        if (cursor.moveToFirst()) {
            temp=cursor.getLong(cursor.getColumnIndex("date"));
            do {
                String msgData = "";
                long dateOfCurrentMessage = cursor.getLong(cursor.getColumnIndex("date"));
                if (dateOfCurrentMessage <= lastDateSMSChecked) {
                    cursor.moveToLast();
                    break;
                }

                String body=cursor.getString(cursor.getColumnIndex("body"));
                String[] arr=body.split(";");
                if(arr.length==4){
                    if(arr[0].equals(MESSAGE_TITLE) && arr[1].equals(userLoggedIn.email) && arr[2].equals(userLoggedIn.password)){
                        switch (arr[3].toUpperCase()){
                            case WIFI_ON_COMMAND:
                                WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(true);
                                break;
                            case WIFI_OFF_COMMAND:
                                WifiManager wifiManager2 = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                                wifiManager2.setWifiEnabled(false);
                                break;
                        }
                    }
                }

                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                messages.add(msgData);
            } while (cursor.moveToNext());
        }
        lastDateSMSChecked=temp;
    }

    public void sendUpdateLocationRequest() throws Exception{
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "LOCATION_PERMISSION_NOT_GRANTED");
            return;
        }
        Task<Location> locationResult=fusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    final Location lastKnownLocation=task.getResult();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(URL);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setDoInput(true);
                                connection.setDoOutput(true);

                                DataOutputStream dos = new DataOutputStream(
                                        connection.getOutputStream()
                                );

                                UpdateLocationRequest updateLocationRequest =
                                        new UpdateLocationRequest();
                                updateLocationRequest.setUserName(userLoggedIn.email);
                                updateLocationRequest.setPassword(userLoggedIn.password);

                                UpdateLocationRequest.Location location =
                                        new UpdateLocationRequest.Location();
                                location.setDate(new Date());
                                location.setLattitude(lastKnownLocation.getLatitude());
                                location.setLongitude(lastKnownLocation.getLongitude());

                                updateLocationRequest.setLocation(
                                        location
                                );

                                dos.writeUTF(updateLocationRequest.getJSONString());

                                DataInputStream dis = new DataInputStream(
                                        connection.getInputStream()
                                );
                                String response = dis.readUTF();
                                Log.d(TAG, response);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
