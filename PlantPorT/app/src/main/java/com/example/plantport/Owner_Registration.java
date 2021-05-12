package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.opengl.ETC1;
import android.os.Build;
import android.os.Bundle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import com.example.plantport4.Model.Reusable_Code;
import com.example.plantport.User.Customer_Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Owner_Registration extends AppCompatActivity {

    EditText Area;
    Spinner City;
    EditText Email;
    EditText First_Name;
    EditText Last_Name;
    EditText Office_Address;
    EditText Password;
    EditText Phone;
    Spinner Place;
    Button Reg;
    Button Sign_in;
    CountryCodePicker ccp;
    FirebaseAuth firebaseAuth;
    String ownerId,ownerID;
    DatabaseReference ref;
    String role = "Owner";
    String state;
    String[] Ernakulam = {"Edappally", "Kalamassery", "Aluva"};
    String[] Idukki = {"Munnar", "Kumuli", "Kattapana"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__registration);

        First_Name = findViewById(R.id.first_name);
        Email = findViewById(R.id.email_owner);
        Password =  findViewById(R.id.password_admin);
        Phone = findViewById(R.id.phone_owner);
        Office_Address = findViewById(R.id.shope_name);
        Area = findViewById(R.id.area);
        Reg = (Button) findViewById(R.id.btnreg_owner);
        Sign_in = (Button) findViewById(R.id.btnsignin_owner);
        City =  findViewById(R.id.city);
        Place = findViewById(R.id.place);
        ccp = (CountryCodePicker) findViewById(R.id.countryCode);

        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Owner");

        ArrayList<String> sp = new ArrayList<>();
        sp.add(0, "Select City");
        sp.add("Ernakulam");
        sp.add("Idukki");

        ArrayList<String> sp1 = new ArrayList<>();
        sp1.add(0, "Select Place");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,sp);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        City.setAdapter(arrayAdapter2);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,sp1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Place.setAdapter(arrayAdapter);


        City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getSelectedItem();
                ((TextView) view).setTextAppearance(R.style.ToolbarTextAppearance);
                ((TextView) view).setTextSize(12);
                state = value.toString().trim();
                ArrayList<String> list = new ArrayList<>();
                if (state.equals("Ernakulam")) {

                    for (String places : Ernakulam) {

                        list.add(places);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Owner_Registration.this, android.R.layout.simple_spinner_dropdown_item, list);
                    Place.setAdapter(arrayAdapter);

                }
                if (state.equals("Idukki")) {

                    for (String places : Idukki) {

                        list.add(places);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Owner_Registration.this, android.R.layout.simple_spinner_dropdown_item, list);
                    Place.setAdapter(arrayAdapter);

                }
                Place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        ((TextView) view).setTextAppearance(R.style.ToolbarTextAppearance);
                        ((TextView) view).setTextSize(12);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                Sign_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Owner_login.class));
                        finish();
                    }
                });

                Reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String fname = First_Name.getText().toString().trim();
                        final String email = Email.getText().toString().trim();
                        final String pwd = Password.getText().toString().trim();
                        final String phn = Phone.getText().toString().trim();
                        final String office_address = Office_Address.getText().toString().trim();
                        final String area = Area.getText().toString().trim();
                        final String city = City.getSelectedItem().toString();
                        final String place = Place.getSelectedItem().toString();


                        if (fname.isEmpty()) {

                            First_Name.setError("Please enter name");
                            First_Name.requestFocus();

                        } else if (email.isEmpty()) {

                            Email.setError("Please enter your email");
                            Email.requestFocus();
                        }else if (pwd.isEmpty()){

                            Password.setError("Please enter your password");
                            Password.requestFocus();

                        } else if (phn.isEmpty()){

                            Phone.setError("Please enter phone number");
                            Phone.requestFocus();

                        }else if (office_address.isEmpty()){

                            Office_Address.setError("Please enter shop name");
                            Password.requestFocus();

                        } else if (area.isEmpty()){

                            Area.setError("Please enter area");
                            Area.requestFocus();

                        } else {
                            ProgressDialog dialog = new ProgressDialog(Owner_Registration.this); // this = YourActivity
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.setTitle("Loading");
                            dialog.setMessage("Loading. Please wait...");
                            dialog.setIndeterminate(true);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.child(Phone.getText().toString()).exists()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(Owner_Registration.this, "Phone Number Already Used!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ownerID = firebaseAuth.getCurrentUser().getUid();
                                                    ref = FirebaseDatabase.getInstance().getReference("User").child(ownerID);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("Role", role);
                                                    ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            HashMap<Object, Object> hashMap1 = new HashMap<>();
                                                            hashMap1.put("First_Name", fname);
                                                            hashMap1.put("Email", email);
                                                            hashMap1.put("Phone", phn);
                                                            hashMap1.put("password", pwd);
                                                            hashMap1.put("Office_Area", office_address);
                                                            hashMap1.put("area", area);
                                                            hashMap1.put("City", city);
                                                            hashMap1.put("Place", place);
                                                            hashMap1.put("OwnerId", ownerID);
//
                                                            FirebaseDatabase.getInstance().getReference("Owner").child(ownerID).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            new AlertDialog.Builder(getApplicationContext());
                                                                            AlertDialog.Builder builder;
                                                                            if (task.isSuccessful()) {
                                                                                dialog.dismiss();
                                                                                builder = new AlertDialog.Builder(Owner_Registration.this);
                                                                                builder.setMessage("You have registered!");
                                                                                builder.setCancelable(false);
                                                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                        dialog.dismiss();
                                                                                        String str = Owner_Registration.this.ccp.getSelectedCountryCodeWithPlus() + phn;
                                                                                        Intent intent = new Intent(Owner_Registration.this, Owner_VerifyPhone.class);
                                                                                        intent.putExtra("phonenumber", str);
                                                                                        startActivity(intent);

                                                                                    }
                                                                                });
                                                                                builder.create().show();

                                                                            } else {

                                                                                dialog.dismiss();
                                                                                Toast.makeText(Owner_Registration.this, "Error", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "EmailId Already Used!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}



