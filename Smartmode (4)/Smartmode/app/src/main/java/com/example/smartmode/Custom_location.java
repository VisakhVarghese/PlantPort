package com.example.smartmode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Custom_location extends AppCompatActivity {

    EditText txt;
    Button btn;
    database db;
    public int btn_no;
    boolean flag = false;
    String nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_location);
        getSupportActionBar().hide();
        db = new database(Custom_location.this);

        txt = findViewById(R.id.cstlocation_txt);
        btn = findViewById(R.id.cstlocation_btn);

        tmp();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                nm = txt.getText().toString();
                boolean updated = db.addLoc(1,btn_no,nm,flag);
                if(updated == true){
                    Toast.makeText(Custom_location.this, "added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Custom_location.this,SetLocation.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Custom_location.this, "Failed", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        });

    }

    public void tmp(){
        Cursor result = db.tmpass();
        result.moveToLast();
        btn_no = result.getInt(0);
    }
}