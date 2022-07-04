package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Owner_ResetPassword extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    private EditText passwordPhone;

    Button otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__reset_password);

        passwordPhone = (EditText) findViewById(R.id.email_reset);
        otp = (Button) findViewById(R.id.btnreset);
        firebaseAuth = FirebaseAuth.getInstance();

        otp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String phone = passwordPhone.getText().toString().trim();

                String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                Query checkUser = FirebaseDatabase.getInstance().getReference("Owner").child(userID).orderByChild("Phone").equalTo(phone);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            passwordPhone.setError(null);
                            passwordPhone.setEnabled(false);

                            Intent i = new Intent(getApplicationContext(), Owner_VerifyPhone.class);
                            i.putExtra("phonenumber", phone);
                            i.putExtra("Imply", "updateData");
                            startActivity(i);

                        } else {

                            passwordPhone.setError("No Such an user exist!");
                            passwordPhone.setEnabled(true);
                            passwordPhone.requestFocus();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                String str = passwordPhone.getText().toString().trim();
//                if (str.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Please enter your registered ", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    firebaseAuth.sendPasswordResetEmail(str).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()) {
//
//                                Toast.makeText(Owner_ResetPassword.this, "Password reset email send", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(Owner_ResetPassword.this, Owner_login.class));
//                                finish();
//
//                            } else {
//
//                                Toast.makeText(getApplicationContext(), "Error in sending password reset", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

            }
        });
    }
}