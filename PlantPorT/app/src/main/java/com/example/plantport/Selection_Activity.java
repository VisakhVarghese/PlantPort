package com.example.plantport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.plantport.Admin.Admin_Login;
import com.example.plantport.User.Customer_Log;

public class Selection_Activity extends AppCompatActivity {

    Button Admin, Owner, User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_);

        Toolbar toolbar2 = findViewById(R.id.toolbar);
        toolbar2.setTitle("Selection Menu");
        toolbar2.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar2);


        User = (Button) findViewById(R.id.btn_user);
        Admin = (Button) findViewById(R.id.btn_admin);
        Owner = (Button) findViewById(R.id.btn_owner);

        User.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(Selection_Activity.this.getApplicationContext(), Customer_Log.class);
                Selection_Activity.this.startActivity(intent);
                Selection_Activity.this.finish();
            }
        });

        Admin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(Selection_Activity.this.getApplicationContext(), Admin_Login.class);
                Selection_Activity.this.startActivity(intent);
            }
        });

        Owner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(Selection_Activity.this.getApplicationContext(), Owner_login.class);
                Selection_Activity.this.startActivity(intent);
            }
        });
    }

}