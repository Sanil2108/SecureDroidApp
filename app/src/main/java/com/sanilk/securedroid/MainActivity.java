package com.sanilk.securedroid;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION=11;
    private static final int MY_PERMISSIONS_READ_SMS=12;

    boolean locationPermissionGranted=true;

    private Location lastKnownLocation;

    private static final String TAG="MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context=this;

        setContentView(R.layout.activity_main);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted=false;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        if(locationPermissionGranted){
            Intent intent=new Intent(context, MainNetworkingService.class);
            startService(intent);
        }


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_READ_SMS);
        }else{
            Intent intent=new Intent(this, MainNetworkingService.class);
            startService(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent=new Intent(this, MainNetworkingService.class);
                    startService(intent);
                } else {
                    finish();
                }
                break;
            case MY_PERMISSIONS_READ_SMS:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(this, MainNetworkingService.class);
                    startService(intent);
                }else{
                    finish();
                }
        }
    }
}
