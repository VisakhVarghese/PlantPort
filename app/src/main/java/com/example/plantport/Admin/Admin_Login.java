package com.example.plantport.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plantport.Model.Admin;
import com.example.plantport.R;
import com.example.plantport.User.Customer_Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login extends AppCompatActivity {

    Button Sign_Up,Sign_In;
    EditText phn,password;
    String dbPhn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        Sign_Up = (Button)findViewById(R.id.admin_signup);
        Sign_In = (Button)findViewById(R.id.admin_signin);
        phn = findViewById(R.id.admin_phn);
        password = findViewById(R.id.admin_pwd);

        reference = FirebaseDatabase.getInstance().getReference("Admin");

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Admin_Registration.class));
                finish();
            }
        });


        Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phone = phn.getText().toString().trim();
                final String pw = password.getText().toString().trim();

                if (phone.length() < 10){

                    phn.setError("Please enter your phone");
                    phn.requestFocus();


                } else if (pw.isEmpty()) {

                    password.setError("Please enter your password");
                    password.requestFocus();

                }else {

                    ProgressDialog dialog = new ProgressDialog(Admin_Login.this); // this = YourActivity
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Loading");
                    dialog.setMessage("Loading. Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child(phone).exists()){

                                    reference.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Admin admin = snapshot.getValue(Admin.class);
                                            assert admin != null;
                                            if (admin.getPassword().equals(pw)){
                                                dialog.dismiss();
                                                Toast.makeText(Admin_Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),Admin_Home.class));
                                                finish();

                                            }else {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Password is wrong!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Phone number is wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }
}