package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantport.Admin.Admin_Home;
import com.example.plantport.Model.Admin;
import com.example.plantport.User.Customer_PhnVerif;
import com.example.plantport.User.Customer_phoneLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Owner_Phone_Login extends AppCompatActivity {

    EditText Phone, otp;
    Button Send_Otp;
    Button Sign_In_With_Email;
    TextView Sign_Up;
    Button Sign_in;
    TextView Text;
    String verificationId;
    DatabaseReference reference;
    CountryCodePicker cpp;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__phone__login);

        Phone =findViewById(R.id.phone_owner_login);
        otp = findViewById(R.id.otp_login);
        Sign_In_With_Email = (Button) findViewById(R.id.btn_withemail);
        Sign_Up = (TextView) findViewById(R.id.signup_Owner);
        Send_Otp = (Button) findViewById(R.id.btn_login_otp);
        Sign_in = (Button) findViewById(R.id.btn_signin_owner);
        cpp = (CountryCodePicker) findViewById(R.id.countryCode_login);
        Text = (TextView) findViewById(R.id.resend_view_login);
        firebaseauth = FirebaseAuth.getInstance();
//        Text.setVisibility(View.INVISIBLE);

        reference = FirebaseDatabase.getInstance().getReference("Owner");

        Send_Otp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(Phone.getText().toString()).exists()){

                            sendOtp();

                        }else {

                            Toast.makeText(getApplicationContext(), "Phone number is not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String phn =  Phone.getText().toString().trim();
                String str2 = otp.getText().toString().trim();

                if (TextUtils.isEmpty(phn)) {

                    Phone.setError("Need Phone Number");
                    Phone.requestFocus();

                } else if (TextUtils.isEmpty(str2)) {

                    Send_Otp.setVisibility(View.VISIBLE);

                    if (str2.length() == 0) {

                        otp.setError("Enter code");
                        otp.requestFocus();
                    }

                    veriyCode(str2);
                    Send_Otp.setVisibility(View.VISIBLE);
                }
            }
        });

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Owner_Registration.class));
                finish();
            }
        });
        Sign_In_With_Email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Owner_login.class));
                finish();
            }
        });
    }

    private void sendOtp() {

        String str = cpp.getDefaultCountryCodeWithPlus() + Phone.getText().toString().trim();

        sendVerificationCode(str);
        Send_Otp.setVisibility(View.INVISIBLE);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

//                        Send_Otp.setVisibility(View.INVISIBLE);
                Text.setVisibility(View.VISIBLE);
                Text.setText("Resend Code With in" + millisUntilFinished / 1000 + "Seconds");

            }

            @Override
            public void onFinish() {

                Send_Otp.setVisibility(View.VISIBLE);
                Text.setVisibility(View.INVISIBLE);

            }
        }.start();
    }


    private void sendVerificationCode(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mcallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60L, TimeUnit.SECONDS, (Activity) TaskExecutors.MAIN_THREAD, this.mcallback);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String str = phoneAuthCredential.getSmsCode();
            if (str != null) {

                otp.setText(str); //Auto Verification
                veriyCode(str);
            }

        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getApplicationContext(),"Verification Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    private void veriyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        checkUser(verificationId,code);
//        SignInwithCredentials(credential);

    }

    private void checkUser(String verificationId, String code) {


        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                    assert user != null;
                    long creationTimestamp = Objects.requireNonNull(user.getMetadata()).getCreationTimestamp();
                    long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
                    if (creationTimestamp == lastSignInTimestamp) {

                        Toast.makeText(getApplicationContext(), "There is no account exist", Toast.LENGTH_SHORT).show();

                    } else {

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        SignInwithCredentials(credential);

//                       startActivity(new Intent(getApplicationContext(),Customer_Home.class));
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(getApplicationContext(), "Code is invalid", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void SignInwithCredentials(PhoneAuthCredential credential) {

        firebaseauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(getApplicationContext(),Owner_Home.class));

                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Owner_login.class));
        finish();
    }
}





