package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.plantport.Common.Common;
import com.example.plantport.User.Customer_Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (new Handler()).postDelayed(new Runnable() {
            public void run() {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Role");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String str = (String) snapshot.getValue(String.class);
                                    assert str != null;
//                                if (str.equals("Owner")) {
//
//                                    startActivity(new Intent(MainActivity.this.getApplicationContext(), Owner_Home.class));
//                                    finish();
//                                }
                                    if (str.equals("Customer")) {

                                        startActivity(new Intent(getApplicationContext(), Customer_Home.class));
                                        finish();
                                    } else {

                                        Toast.makeText(MainActivity.this, "There is no Current User!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this.getApplicationContext(), Selection_Activity.class);
                                        MainActivity.this.startActivity(intent);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            Toast.makeText(MainActivity.this, "Verify email!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this.getApplicationContext(), Selection_Activity.class);
                            MainActivity.this.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "There is no Current User!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this.getApplicationContext(), Selection_Activity.class);
                        MainActivity.this.startActivity(intent);
                    }
                }else {

                    Toast.makeText(MainActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }


        }, 4000);
    }


}