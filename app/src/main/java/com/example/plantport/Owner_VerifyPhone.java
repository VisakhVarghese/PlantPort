package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Owner_VerifyPhone extends AppCompatActivity {

    TextInputEditText Otp;
    Button Resend_otp;
    TextView Text;
    Button Verify;
    CountryCodePicker cpp;
    FirebaseAuth firebaseAuth;
    String verificationId,phonenumber,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__verify_phone);

                phonenumber =getIntent().getStringExtra("phonenumber");
                data = getIntent().getStringExtra("Imply");


                Otp = (TextInputEditText)findViewById(R.id.otp_owner);
                Resend_otp = (Button)findViewById(R.id.btn_resend_otp);
                Verify = (Button)findViewById(R.id.btn_verify);
                Text = (TextView)findViewById(R.id.plane_txt);
                Resend_otp.setVisibility(View.INVISIBLE);
                Text.setVisibility(View.INVISIBLE);

                 firebaseAuth = FirebaseAuth.getInstance();

                sendverificationcode(phonenumber);

                Verify.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String str = Objects.requireNonNull(Otp.getText()).toString().trim();
                        Resend_otp.setVisibility(View.INVISIBLE);

                        if (str.length() == 0) {

                            Otp.setError("Enter code");
                            Otp.requestFocus();
                            return;
                        }
                        veriyCode(str);
                    }
                });


        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Resend_otp.setVisibility(View.VISIBLE);
                Text.setText("Resend Code With in" +millisUntilFinished/1000+"Seconds");

            }

            @Override
            public void onFinish() {

                Resend_otp.setVisibility(View.INVISIBLE);
                Text.setVisibility(View.INVISIBLE);

            }
        }.start();

                Resend_otp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Resend_otp.setVisibility(View.INVISIBLE);
                        resendOtp(phonenumber);
                        
                        new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                Resend_otp.setVisibility(View.VISIBLE);
                                Text.setText("Resend Code With in" +millisUntilFinished/1000+"Seconds");

                            }

                            @Override
                            public void onFinish() {

                                Resend_otp.setVisibility(View.INVISIBLE);
                                Text.setVisibility(View.INVISIBLE);

                            }
                        }.start();

                    }
                });
            }

    private void resendOtp(String phonen) {

        sendverificationcode(phonen);
    }

    private void sendverificationcode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60L, TimeUnit.SECONDS, (Activity) TaskExecutors.MAIN_THREAD, this.mcallback);
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

                    Toast.makeText(Owner_VerifyPhone.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }
            };

    private void veriyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        SignInwithCredentials(credential);

    }

    private void SignInwithCredentials(PhoneAuthCredential credential) {

        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    if (data.equals("Imply")){

                        updateUserData();

                    }else {

                        startActivity(new Intent(getApplicationContext(), Owner_login.class));
                        finish();
                    }

                } else {

                    Toast.makeText(Owner_VerifyPhone.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void updateUserData() {

        Intent i = new Intent(getApplicationContext(),SetNewPassword.class);
        i.putExtra("phoneNo",phonenumber);
        startActivity(i);
        finish();

    }

}