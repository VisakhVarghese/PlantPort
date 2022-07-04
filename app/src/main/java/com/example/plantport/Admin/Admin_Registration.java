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
import com.example.plantport.Model.Users;
import com.example.plantport.R;
import com.example.plantport.User.Customer_Log;
import com.example.plantport.User.Customer_PhnVerif;
import com.example.plantport.User.Customer_Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;

public class Admin_Registration extends AppCompatActivity {

    Button Sign_up,Sign_In;
    EditText name,email,password,phone;
    CountryCodePicker ccp;
    DatabaseReference reference,ref;
    FirebaseAuth mAuth;
    String role = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__registration);

        Sign_up = findViewById(R.id.admin_rsigup);
        Sign_In = findViewById(R.id.admin_rsigin);
        name = findViewById(R.id.admin_name);
        email = findViewById(R.id.admin_email);
        phone = findViewById(R.id.admin_rphone);
        password = findViewById(R.id.admin_rpwd);
        ccp = findViewById(R.id.admin_ccp);

        reference = FirebaseDatabase.getInstance().getReference("Admin");

        Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Admin_Login.class));
                finish();
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getText().toString().trim();
                final String em = email.getText().toString().trim();
                final String phn = phone.getText().toString().trim();
                final String pw = password.getText().toString().trim();

                if (Name.isEmpty()) {

                    name.setError("Please enter your name");
                    name.requestFocus();

                } else if (em.isEmpty()) {

                    email.setError("Please enter your email id");
                    email.requestFocus();

                } else if (pw.isEmpty() && pw.length() < 10) {

                    password.setError("Please enter your password");
                    password.requestFocus();

                }else if (phn.length() < 10){

                    phone.setError("Please enter your password");
                    phone.requestFocus();

                }else {

                    ProgressDialog dialog = new ProgressDialog(Admin_Registration.this); // this = YourActivity
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Loading");
                    dialog.setMessage("Loading. Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    reference.orderByChild("phone").equalTo(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Phone Number Already Used!", Toast.LENGTH_SHORT).show();

                            }else {

//                                String adminId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                ref = FirebaseDatabase.getInstance().getReference("User").child(phone.getText().toString());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Role", role);

                                ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
//                                        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        Admin admin = new Admin(name.getText().toString(), phone.getText().toString(), email.getText().toString(), password.getText().toString());
                                        reference.child(phone.getText().toString()).setValue(admin);
                                        Toast.makeText(getApplicationContext(), "Register Successful!", Toast.LENGTH_SHORT).show();
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(ccp.getSelectedCountryCodeWithPlus());
                                        stringBuilder.append(phn);
//                                        str = stringBuilder.toString();
                                        Intent intent = new Intent(getApplicationContext(), Admin_Login.class);
//                                        intent.putExtra("PhoneNumber", str);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Admin_Login.class));
        finish();
    }
}