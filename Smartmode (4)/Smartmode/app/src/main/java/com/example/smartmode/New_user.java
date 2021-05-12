package com.example.smartmode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.ls.LSResourceResolver;

public class New_user extends AppCompatActivity {

    database db;
    EditText edt;
    Button btn,lcbtn;
    boolean flag = false;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        getSupportActionBar().hide();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(New_user.this);

      //  location_permission();

        db = new database(New_user.this);
        edt = findViewById(R.id.newuser_txt);
        btn = findViewById(R.id.newuser_btn);
        lcbtn = findViewById(R.id.newuser_btnlc);
        edt.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        btns();
        locSet();


        lcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(ActivityCompat.checkSelfPermission(New_user.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(New_user.this
                , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //gcl();


                }
                else{

                 */
                    ActivityCompat.requestPermissions(New_user.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    }
                    ,100);

                    
                //}
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults.length > 0 &&  (grantResults[0] + grantResults[1]
        == PackageManager.PERMISSION_GRANTED)){
           // gcl();
            lcbtn.setVisibility(View.GONE);
            edt.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(this, "Permission not accessed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void gcl(){
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){

                    }
                    else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();

                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                        ,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }



    public void add(View view) {
        flag = true;
        boolean updated = db.newUser(edt.getText().toString(),flag);
        if(updated == true) {

            Toast.makeText(getApplicationContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(New_user.this, SetLocation.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Data updated Failed", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }
    public void btns(){
        flag = true;
        for(int i=1;i<=6;i++) {
            boolean updated = db.btnSet(0, flag);
           // if(updated==true){
            //    Toast.makeText(this, "btn added "+i, Toast.LENGTH_SHORT).show();
         //   }
         //   else
         //       Toast.makeText(this, "failed at "+i, Toast.LENGTH_LONG).show();
        }
        db.close();


    }

    public void locSet(){
        flag = true;
        for(int i=1;i<=6;i++) {
            boolean updated = db.setEnable(0);
        }
        db.close();
    }
}