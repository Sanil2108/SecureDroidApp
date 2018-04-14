package com.sanilk.securedroid;

import android.*;
import android.Manifest;
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
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.security.Permission;

public class LocationActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION=11;

    boolean locationPermissionGranted=true;

    private Location lastKnownLocation;

    private static final String TAG="LOCATION_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted=false;
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
//        }
//        if(locationPermissionGranted){
//            Task<Location> locationResult=fusedLocationProviderClient.getLastLocation();
//            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if(task.isSuccessful()){
//                        lastKnownLocation=task.getResult();
//                        Log.d(TAG, "lat-"+lastKnownLocation.getLatitude()+", long-"+lastKnownLocation.getLongitude());
//                    }
//                }
//            });
//        }

    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationPermissionGranted=true;
//                } else {
//                    finish();
//                }
//                break;
//        }
//    }
}
