package com.example.smartmode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Add_Location extends AppCompatActivity {

    Button ib_1;
    Button ib_2;
    Button ib_3;
    Button ib_4;
    boolean flag = false;
    database db;
    public int bn;
    int btn_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__location);
        getSupportActionBar().hide();
        db = new database(Add_Location.this);
        Toast.makeText(this, "btn"+ bn, Toast.LENGTH_SHORT).show();

       // Cursor result = db.tmpass();
       // result.moveToFirst();
       // bn = Integer.parseInt(result.getString(0));


        Cursor result = db.tmpass();
        if(result.getCount()==0){
            Toast.makeText(this, "noo values from tmpass()", Toast.LENGTH_SHORT).show();
        }
        else {
            result.moveToLast();
            bn = result.getInt(0);
            Toast.makeText(this, "btn value ******** = "+ bn, Toast.LENGTH_SHORT).show();
        }


       ib_1 = findViewById(R.id.imageButton9);
       ib_2 = findViewById(R.id.imageButton10);
       ib_3 = findViewById(R.id.imageButton11);
       ib_4 = findViewById(R.id.imageButton12);

       ib_1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               flag = true;
             //  Toast.makeText(Add_Location.this, "btn"+bn, Toast.LENGTH_SHORT).show();
               boolean updated = db.addLoc(1,bn,"Home",flag);
               if(updated == true){
                   Toast.makeText(Add_Location.this, "added Successfully   "+bn, Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(Add_Location.this,SetLocation.class);
                   startActivity(intent);
                   finish();
               }
               else{
                   Toast.makeText(Add_Location.this, "Failed", Toast.LENGTH_SHORT).show();
               }

             //  db.close();
           }
       });

       ib_2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               flag = true;
               boolean updated = db.addLoc(1,bn,"Office",flag);
               if(updated == true){
                   Toast.makeText(Add_Location.this, "added Successfully", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(Add_Location.this,SetLocation.class);
                   startActivity(intent);
                   finish();
               }
               else{
                   Toast.makeText(Add_Location.this, "Failed", Toast.LENGTH_SHORT).show();
               }

               db.close();
           }
       });

        ib_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                boolean updated = db.addLoc(1,bn,"School",flag);
                if(updated == true){
                    Toast.makeText(Add_Location.this, "added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Add_Location.this,SetLocation.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Add_Location.this, "Failed", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        });
        ib_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Location.this,Custom_location.class);
                startActivity(intent);
            }
        });


    }

}