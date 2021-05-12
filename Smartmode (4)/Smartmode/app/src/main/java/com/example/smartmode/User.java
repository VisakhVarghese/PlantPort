package com.example.smartmode;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class User extends AppCompatActivity {


    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        database db = new database(this);
        txt = findViewById(R.id.usertxt);

        Cursor result = db.startData();
        result.moveToFirst();
        //StringBuilder sb = new StringBuilder();
        //sb.append(result.getString(0));
       // if(result.getCount()!=0){
       //     result.moveToFirst();
            txt.setText(result.getString(0));
        //}

       // txt.setText(sb);



            //String rs = result.getString(0);
           // Toast.makeText(this, ""+ rs, Toast.LENGTH_SHORT).show();




    }
}