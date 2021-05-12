package com.example.smartmode;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    database db;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new database(this);
        getSupportActionBar().hide();

        getall();

    }

    public void  getall(){
        Cursor result = db.startData();

        if (result.getCount()!=0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, SetLocation.class);
                    startActivity(intent);
                    finish();

                }
            }, 3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, New_user.class);
                    startActivity(intent);
                    finish();

                }
            }, 3000);
        }
        db.close();
    }
}