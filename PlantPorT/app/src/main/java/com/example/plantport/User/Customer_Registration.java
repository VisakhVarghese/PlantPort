package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantport.Model.Users;
import com.example.plantport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer_Registration extends AppCompatActivity {

    CountryCodePicker Code;
    FirebaseDatabase database;
    EditText email;
    FirebaseAuth mFirebaseAuth;
    EditText name;
    EditText phone;
    EditText pwd;
    DatabaseReference reff;
    Button register;
    String role = "Customer";
    TextView sin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__registration);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase;
        reff = firebaseDatabase.getReference("Customer");
        Code = (CountryCodePicker) findViewById(R.id.ccode);
        name = (EditText) findViewById(R.id.first_name);
        email = (EditText) findViewById(R.id.txtphn_customer);
        pwd = (EditText) findViewById(R.id.txt_pwd);
        phone = (EditText) findViewById(R.id.text_phn);
        sin = (TextView)findViewById(R.id.loginhere);
        register = (Button) findViewById(R.id.btnreg_customer);

                sin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param1View) {
                        Intent intent = new Intent(Customer_Registration.this, Customer_Log.class);
                        startActivity(intent);
                    }
                });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ProgressDialog dialog = new ProgressDialog(getApplicationContext());

                String str2 = name.getText().toString().trim();
                final String em = email.getText().toString().trim();
                final String phn = phone.getText().toString().trim();
                final String pw = pwd.getText().toString().trim();

                if (str2.isEmpty()) {

                    name.setError("Please enter your name");
                    name.requestFocus();

                } else if (em.isEmpty()) {

                    email.setError("Please enter your email id");
                    email.requestFocus();

                }else if (isValid(em)){

                    email.setError("Invalid Email");
                    email.requestFocus();

                } else if (pw.isEmpty()) {

                    pwd.setError("Please enter your phone number");
                    pwd.requestFocus();

                }else if (phn.isEmpty()) {

                    phone.setError("Please enter your password");
                    phone.requestFocus();

                }else if (phn.length() < 10 || phn.length() > 10) {

                    phone.setError("Please enter 10 digit phone number");
                    phone.requestFocus();

                }else

                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("Loading");
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();

                    reff.orderByChild("phone").equalTo(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                        }

                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                dialog.dismiss();
                                Toast.makeText(Customer_Registration.this, "Phone Number Already Used!", Toast.LENGTH_SHORT).show();

                            } else {
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(em, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task1) {

                                                if (task1.isSuccessful()) {

                                                    String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(str);
                                                    HashMap<Object, Object> hashMap = new HashMap<>();
                                                    hashMap.put("Role", role);

                                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        public void onComplete(Task<Void> task2) {

                                                            dialog.dismiss();
                                                            String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                            Users users = new Users(str, name.getText().toString(), email.getText().toString(), pwd.getText().toString(), phone.getText().toString());
                                                            reff.child(str).setValue(users);
                                                            Toast.makeText(Customer_Registration.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                                                            StringBuilder stringBuilder = new StringBuilder();
                                                            stringBuilder.append(Code.getSelectedCountryCodeWithPlus());
                                                            stringBuilder.append(phn);
                                                            str = stringBuilder.toString();
                                                            Intent intent = new Intent(getApplicationContext(), Customer_Log.class);
                                                            intent.putExtra("phonenumber", str);
                                                            startActivity(intent);

                                                            finish();
                                                        }
                                                    });
                                                } else {

                                                    dialog.dismiss();
                                                    Toast.makeText(Customer_Registration.this, "EmailId Already Used!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
            }
        });
    }

    private boolean isValid(String em) {

        boolean isValid = true;
        String expression ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(em);
        if (matcher.matches()) {
            isValid = false;
        }
        return isValid;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Customer_Log.class));
        finish();
    }
}