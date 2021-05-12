package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantport.Owner_Home;
import com.example.plantport.Owner_Registration;
import com.example.plantport.Owner_login;
import com.example.plantport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
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

public class Customer_phoneLogin extends AppCompatActivity {

    EditText Phone, otp;
    Button send_otp;
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
        setContentView(R.layout.activity_customer_phone_login);

        Phone =findViewById(R.id.phone_cu_login);
        otp = findViewById(R.id.otp_log);
        Sign_In_With_Email = (Button) findViewById(R.id.btnwithemail);
        Sign_Up = (TextView) findViewById(R.id.signup_customer);
        send_otp = (Button) findViewById(R.id.signin_otp);
        Sign_in = (Button) findViewById(R.id.btnsignin_customer);
        cpp = (CountryCodePicker) findViewById(R.id.countryC_login);
        Text = (TextView) findViewById(R.id.resend);
        firebaseauth = FirebaseAuth.getInstance();
//        Text.setVisibility(View.INVISIBLE);

        reference = FirebaseDatabase.getInstance().getReference("Customer");

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn = Phone.getText().toString().trim();

                if (phn.isEmpty()) {

                    Phone.setError("Please enter your password");
                    Phone.requestFocus();

                } else if (phn.length() < 10 || phn.length() > 10) {

                    Phone.setError("Please enter 10 digit phone number");
                    Phone.requestFocus();

                } else {

                    String str = cpp.getDefaultCountryCodeWithPlus() + Phone.getText().toString().trim();

                    sendVerificationCode(str);
                    send_otp.setVisibility(View.INVISIBLE);

                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            Text.setVisibility(View.VISIBLE);
                            Text.setText("Resend Code With in" + millisUntilFinished / 1000 + "Seconds");

                        }

                        @Override
                        public void onFinish() {

                            send_otp.setVisibility(View.VISIBLE);
                            Text.setVisibility(View.INVISIBLE);

                        }
                    }.start();
                }
            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String phn = Phone.getText().toString().trim();
                String code = otp.getText().toString().trim();

                 if (phn.isEmpty()) {

                    Phone.setError("Please enter your password");
                    Phone.requestFocus();

                }else if (phn.length() < 10 || phn.length() > 10) {

                    Phone.setError("Please enter 10 digit phone number");
                    Phone.requestFocus();

                } else if (code.length() == 0) {

                    otp.setError("Enter code");
                    otp.requestFocus();
                } else {

                    veriyCode(code);
                    send_otp.setVisibility(View.VISIBLE);
                }
            }

        });

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Customer_Registration.class));
                finish();
            }
        });
        Sign_In_With_Email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Customer_Log.class));
                finish();
            }
        });
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


            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                otp.setText(code); //Auto Verification
                veriyCode(code);
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
//        checkUser(verificationId,code);
        SignInwithCredentials(credential);

    }

//    private void checkUser(String verificationId, String code) {
//
//        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
//        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
//                    assert user != null;
//                    String uid = user.getUid();
//                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Customer").child(uid);
//                    reff.orderByChild("phone").equalTo(Phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            if (snapshot.getValue() != null){
//
//                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//                                SignInwithCredentials(credential);
//
//                            }else{
//
//                                Toast.makeText(Customer_phoneLogin.this, "There is no account exist", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
////                    assert user != null;
//                    assert user != null;
//                    long creationTimestamp = Objects.requireNonNull(user.getMetadata()).getCreationTimestamp();
//                    long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
//                    if (creationTimestamp == lastSignInTimestamp) {
//
//                        Toast.makeText(getApplicationContext(), "There is no account exist", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//                        SignInwithCredentials(credential);
//
////                       startActivity(new Intent(getApplicationContext(),Customer_Home.class));
//                    }
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                        Toast.makeText(getApplicationContext(), "Code is invalid", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });
//    }

    private void SignInwithCredentials(PhoneAuthCredential credential) {

        firebaseauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

//                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
//                    assert user != null;
//                    long creationTimestamp = Objects.requireNonNull(user.getMetadata()).getCreationTimestamp();
//                    long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
//                    if (creationTimestamp == lastSignInTimestamp) {
//
//                        Toast.makeText(getApplicationContext(), "There is no account exist", Toast.LENGTH_SHORT).show();
//
//                    } else {

                        startActivity(new Intent(getApplicationContext(), Owner_Home.class));

                        finish();
//                    }


                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(getApplicationContext(), "Code is invalid"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
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





