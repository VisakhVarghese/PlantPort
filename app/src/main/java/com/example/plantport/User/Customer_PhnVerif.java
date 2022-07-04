package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.plantport4.Model.Reusable_Code;
import com.example.plantport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Customer_PhnVerif extends AppCompatActivity {

    EditText Otp;
    Button Resend_otp;
    TextView Text;
    Button Verify;
    FirebaseAuth firebaseAuth;
    String phonenumber, verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__phn_verif);

        phonenumber = (getIntent().getStringExtra("phonenumber"));
        Otp =  findViewById(R.id.otp_customer);
        Resend_otp = (Button) findViewById(R.id.btnresendotp_customer);
        Verify = (Button) findViewById(R.id.btn_verify_customer);
        firebaseAuth = FirebaseAuth.getInstance();
        Text = (TextView) findViewById(R.id.plane_text_customer);

        Resend_otp.setVisibility(View.INVISIBLE);
        Text.setVisibility(View.INVISIBLE);

        sendverificationcode(phonenumber);

        Verify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String str = Otp.getText().toString().trim();
                Resend_otp.setVisibility(View.INVISIBLE);

                if (str.length() == 0) {
                    Otp.setError("Enter code");
                    Otp.requestFocus();
                }
                veriyCode(str);
            }
        });
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

//                Resend_otp.setVisibility(View.INVISIBLE);
                Text.setText("Resend Code With in" + millisUntilFinished / 1000 + "Seconds");

            }

            @Override
            public void onFinish() {

                Resend_otp.setVisibility(View.VISIBLE);
                Text.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend_otp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Resend_otp.setVisibility(View.INVISIBLE);

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

//                        Resend_otp.setVisibility(View.INVISIBLE);
                        sendverificationcode(phonenumber);
                        Text.setText("Resend Code With in" + millisUntilFinished / 1000 + "Seconds");

                    }

                    @Override
                    public void onFinish() {

                        Resend_otp.setVisibility(View.VISIBLE);
                        Text.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });
    }

    private void sendverificationcode(String num) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(num)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mcallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String str = phoneAuthCredential.getSmsCode();
            if (str != null) {

                Otp.setText(str); //Auto Verification
                veriyCode(str);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Customer_PhnVerif.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    private void veriyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        SignInwithCredentials(credential);

    }

    private void SignInwithCredentials(PhoneAuthCredential credential) {

        Objects.requireNonNull(firebaseAuth.getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();


                    Toast.makeText(Customer_PhnVerif.this, "Verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Customer_Log.class));
                    finish();

                } else {

                    Toast.makeText(Customer_PhnVerif.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}