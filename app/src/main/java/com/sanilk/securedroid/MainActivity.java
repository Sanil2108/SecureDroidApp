package com.sanilk.securedroid;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.sanilk.securedroid.ui_fragments.AlarmFragment;
import com.sanilk.securedroid.ui_fragments.HomeFragment;
import com.sanilk.securedroid.ui_fragments.LocationFragment;
import com.sanilk.securedroid.ui_fragments.SettingsFragment;
import com.sanilk.securedroid.ui_fragments.WifiFragment;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION=11;
    private static final int MY_PERMISSIONS_READ_SMS=12;
    private static final int ALL_PERMISSIONS=13;

    boolean locationPermissionGranted=true;

    private Location lastKnownLocation;

    private static final String TAG="MAIN_ACTIVITY";

    private final String[] PERMISSIONS=new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS
    };

    private String email;
    private String password;

    Intent intent;

    GoogleApiClient googleApiClient;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout=findViewById(R.id.drawerLayout);


        GoogleSignInAccount googleSignInAccount=getIntent().getParcelableExtra("GOOGLE_ACCOUNT");
        email=googleSignInAccount.getEmail();
        password=getIntent().getStringExtra("PASSWORD");

        final Context context=this;

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();

                switch (item.getItemId()){
                    case R.id.nav_bar_home:
                        HomeFragment homeFragment=new HomeFragment();
                        ft.replace(R.id.content_frame, homeFragment);
                        break;
                    case R.id.nav_bar_alarm:
                        AlarmFragment alarmFragment=new AlarmFragment();
                        ft.replace(R.id.content_frame, alarmFragment);
                        break;
                    case R.id.nav_bar_location:
                        LocationFragment locationFragment=new LocationFragment();
                        ft.replace(R.id.content_frame, locationFragment);
                        break;
                    case R.id.nav_bar_wifi:
                        WifiFragment wifiFragment=new WifiFragment();
                        ft.replace(R.id.content_frame, wifiFragment);
                        break;
                    case R.id.nav_bar_settings:
                        SettingsFragment settingsFragment=new SettingsFragment();
                        ft.replace(R.id.content_frame, settingsFragment);
                        break;
                    case R.id.nav_bar_logout:
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
                                        stopService(intent);
                                        finish();
                                    }
                                }
                        );
                }

                ft.commit();

                return true;
            }
        });

        if(!checkPermissions()){
            ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_PERMISSIONS);
        }else{
            intent=new Intent(this, MainNetworkingService.class);
            intent.putExtra("EMAIL", email);
            intent.putExtra("PASSWORD", password);
            startService(intent);
        }

//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted=false;
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
//        }
//
//
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
//                    MY_PERMISSIONS_READ_SMS);
//        }
//
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)==PackageManager.PERMISSION_GRANTED){
//            Intent intent2=new Intent(this, LoginActivity.class);
//            startActivity(intent2);
//        }
    }

    private boolean checkPermissions(){
        for(String s:PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, s)!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)==PackageManager.PERMISSION_GRANTED){
//                        Intent intent2=new Intent(this, LoginActivity.class);
//                        startActivity(intent2);
                    }
                } else {
                    finish();
                }
                break;
            case MY_PERMISSIONS_READ_SMS:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)==PackageManager.PERMISSION_GRANTED){
//                        Intent intent2=new Intent(this, LoginActivity.class);
//                        startActivity(intent2);
                    }
                }else{
                    finish();
                }
                break;
            case ALL_PERMISSIONS:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
//                    Intent intent=new Intent(this, LoginActivity.class);
//                    startActivity(intent);
                    intent=new Intent(this, MainNetworkingService.class);
                    intent.putExtra("EMAIL", email);
                    startService(intent);
                }
                break;
        }
    }
}
