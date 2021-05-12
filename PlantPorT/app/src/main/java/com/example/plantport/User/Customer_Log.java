
package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.plantport4.Model.Users;
import com.example.plantport.Make_Selection;
import com.example.plantport.Model.Users;
import com.example.plantport.Owner_login;
import com.example.plantport.R;
import com.example.plantport.Selection_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Customer_Log extends AppCompatActivity {

    FirebaseDatabase database;
    TextView forgotpassword;
    FirebaseAuth mFirebaseAuth;
    private EditText phone;
    private EditText pwd;
    DatabaseReference reff;
    Button register,sinPhone;
    Button sin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__log);

        mFirebaseAuth = FirebaseAuth.getInstance();
        phone = findViewById(R.id.txt_email_customer);
        pwd = (EditText) findViewById(R.id.txt_pwd_customer);
        register = (Button) findViewById(R.id.btnreg_owner);
        sin = (Button) findViewById(R.id.btnlogin_owner);
        sinPhone = findViewById(R.id.btnlogin_phone);
        forgotpassword = (TextView) findViewById(R.id.txtvpwd_owner);

        database = FirebaseDatabase.getInstance();
        FirebaseAuth.getInstance();

        reff = this.database.getReference().child("Customer");

        sinPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Customer_phoneLogin.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Customer_Registration.class));
                finish();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Hello world").show();
//                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//                Typeface face=Typeface.createFromAsset(getAssets(),"fonts/FONT");
//                textView.setTypeface(face);

                EditText resetMail = new EditText(v.getContext());
                Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat);
                TextView content = new TextView(getApplicationContext());
                content.setTypeface(font);

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Reset Password");
                dialog.setMessage("Enter your email address");
                dialog.setCancelable(true);
                resetMail.setTypeface(font);
                dialog.setView(resetMail);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().sendPasswordResetEmail(resetMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())

                                    Toast.makeText(Customer_Log.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Customer_Log.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                dialog.create().show();
            }

        });
        sin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String ep = phone.getText().toString();
                final String pw = pwd.getText().toString();
//                final ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
//                mDialog.setMessage("Please waiting.....");
//                        mDialog.show();

                if (isValid(ep)){

                    phone.setError("Invalid Email");
                    phone.requestFocus();

                } else if (pw.isEmpty()) {

                    pwd.setError("Please enter password");
                    pwd.requestFocus();

                } else {


                    ProgressDialog dialog = new ProgressDialog(Customer_Log.this); // this = YourActivity
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Loading");
                    dialog.setMessage("Loading. Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    mFirebaseAuth.signInWithEmailAndPassword(ep,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                        }
                    });

                    mFirebaseAuth.signInWithEmailAndPassword(ep, pw).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                reff.addValueEventListener(new ValueEventListener() {
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }

                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                        dialog.dismiss();
                                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                                        assert user != null;
                                        String userId = user.getUid();
//
                                        reff.orderByChild("password").equalTo(pwd.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {

                                                            dialog.dismiss();

                                                            Toast.makeText(Customer_Log.this, "Sign in successfuly!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Customer_Log.this, Customer_Home.class);
                                                            startActivity(intent);
                                                            finish();

                                                        } else {

                                                            dialog.dismiss();
                                                            Toast.makeText(Customer_Log.this, "Email or Password Incorrect!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                });
                            } else {

                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "There is no account exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
                }
            }
        });
    }

    private boolean isValid(String ep) {

        boolean isValid = true;
        String expression ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ep);
        if (matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Selection_Activity.class));
        finish();
    }
}