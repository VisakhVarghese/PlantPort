package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.example.plantport.Common.Common;
import com.example.plantport.Model.Owner;
import com.example.plantport.User.Customer_Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneMultiFactorAssertion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Owner_login extends AppCompatActivity {

    TextView Forgot_Pwd;
    EditText Password;
    EditText Phone_Number;
    EditText reset_email;
    Button Register;
    Button Sign_Phone;
    Button Sign_in;
    String ph, pwd;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        try {

            Register = (Button) findViewById(R.id.btnreg_admin);
            Sign_in = (Button) findViewById(R.id.btnlogin_admin);
            Forgot_Pwd = (TextView) findViewById(R.id.txtvpwd_admin);
            Phone_Number = (EditText) findViewById(R.id.textemail_owner);
            Password = (EditText) findViewById(R.id.textpwd_Owner);
//            Sign_Phone = (Button) findViewById(R.id.btnemailsignin_Owner);
            firebaseAuth = FirebaseAuth.getInstance();

            reference = FirebaseDatabase.getInstance().getReference().child("Owner");

            Sign_in.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ph = Phone_Number.getText().toString().trim();
                    pwd = Password.getText().toString().trim();

                    if (isValid(ph)) {

                        Phone_Number.setError("Invalid Email");
                        Phone_Number.requestFocus();

                    } else if (pwd.isEmpty()) {

                        Password.setError("Please enter your password");
                        Password.requestFocus();

                    }else{
                         if (Common.isConnectedToInternet(getBaseContext())) {

                             ProgressDialog dialog = new ProgressDialog(Owner_login.this); // this = YourActivity
                             dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                             dialog.setTitle("Loading");
                             dialog.setMessage("Loading. Please wait...");
                             dialog.setIndeterminate(true);
                             dialog.setCanceledOnTouchOutside(false);
                             dialog.show();

                             firebaseAuth.signInWithEmailAndPassword(ph, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {

                                     if (task.isSuccessful()) {
                                         if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {

                                             FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                                             assert user != null;
                                             String ownerId = user.getUid();
                                             reference.child(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                     Owner owner = snapshot.getValue(Owner.class);

                                                     assert owner != null;
                                                     if (owner.getPassword().equals(pwd)) {
                                                         dialog.dismiss();
                                                         FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                                         assert user1 != null;
                                                         owner.setPassword(pwd);
                                                         user1.updatePassword(pwd);

                                                         Toast.makeText(Owner_login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                                         Intent intent = new Intent(Owner_login.this, Owner_Home.class);
                                                         startActivity(intent);
                                                         finish();
                                                     } else {
                                                         dialog.dismiss();
                                                         Toast.makeText(Owner_login.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                                         return;
                                                     }
                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });

                                         } else {
                                                dialog.dismiss();
                                             Toast.makeText(Owner_login.this.getApplicationContext(), "Please Verify EmailId", Toast.LENGTH_SHORT).show();
                                         }
                                     } else {
                                         dialog.dismiss();
                                         Toast.makeText(Owner_login.this.getApplicationContext(), "There is no account exist!", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                             });
                         }else {
                             Toast.makeText(Owner_login.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                         }
                    }
                }
            });
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Owner_login.this, Owner_Registration.class);
                    startActivity(intent);

                }
            });

            Forgot_Pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText resetMail = new EditText(v.getContext());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Reset Password");
                    dialog.setMessage("Enter your email address");
                    dialog.setView(resetMail);

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            FirebaseAuth.getInstance().sendPasswordResetEmail(resetMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())

                                    Toast.makeText(Owner_login.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Owner_login.this, "Email is not valid", Toast.LENGTH_SHORT).show();
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

//            Sign_Phone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    startActivity(new Intent(getApplicationContext(), Owner_Phone_Login.class));
//                    finish();
//
//                }
//            });
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid(String ph) {

        boolean isValid = true;
        String expression ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ph);
        if (matcher.matches()) {
            isValid = false;
        }
        return isValid;

    }

//    String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//    public boolean isValid() {
//
//        Phone_Number.setEnabled(false);
//        Phone_Number.setError("");
//        Password.setEnabled(false);
//        Password.setError("");
//
//        boolean isvalid = false, isvalidemail = false, isvalidpassword = false;
//
//        if (TextUtils.isEmpty(ph)) {
//
//            Phone_Number.setEnabled(true);
//            Phone_Number.setError("Phone number is required");
//
//        } else {
//
//            if (ph.matches(email_pattern)) {
//
//                isvalidemail = true;
//
//            } else {
//
//                Phone_Number.setEnabled(true);
//                Phone_Number.setError("inValid Email");
//            }
//        }
//        if (TextUtils.isEmpty(pwd)) {
//
//            this.Password.setEnabled(true);
//            this.Password.setError("Password is required");
//
//        } else {
//
//            isvalidpassword = true;
//        }
//
//        isvalid = (isvalidemail && isvalidpassword) ? true : false;
//
//        return isvalid;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Selection_Activity.class));
        finish();

    }
}