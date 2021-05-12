package com.example.smartmode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartmode.Model.Button_Status;
import com.example.smartmode.Model.Location;

import java.util.ArrayList;
import java.util.List;

public class location_settings extends AppCompatActivity {

    int ACTIVE_STATE = 1;

    ImageView ib;
    TextView txt;
    database db;
    String state;
    Button delete;
    List<Button_Status>button_statusList;
    List<Location>locationList;
    SwitchCompat s1,s2,s3,s4;
    static boolean isTouched = false;

    public int btn_no;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_settings);

        getSupportActionBar().hide();
        db = new database(location_settings.this);
        ib = findViewById(R.id.imageView5);
        txt = findViewById(R.id.textView7);
        s1 = findViewById(R.id.switch1);
        s2 = findViewById(R.id.switch2);
        s3 = findViewById(R.id.switch3);
        s4 = findViewById(R.id.switch4);
        delete = findViewById(R.id.buttondel);
        state = getIntent().getStringExtra("State");
        button_statusList = new ArrayList<>();

        checkSettings();
//        tmp();
//        nme();

        button_statusList = new database(getApplicationContext()).getBtnDetails();
        for (int i = 0; i < button_statusList.size(); i++) {
            String ID = button_statusList.get(i).getBTN_ID();
            if (ID.equals(state)) {

                s1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        isTouched = true;
                        return false;
                    }
                });

                final int finalI = i;
                s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isTouched) {
                            isTouched = false;

                            boolean updated;
                            if (isChecked) {

                                updated = db.setStatus(true, state);

                            } else {

                                updated = db.setStatus(false, state);
                            }
                            if (updated) {

                                Toast.makeText(location_settings.this, "added Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(location_settings.this, "Failed", Toast.LENGTH_SHORT).show();
                            }

                            checkState(finalI);
                        }
                    }
                });

                s4.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        Cursor result = db.locState();
                        result.moveToPosition(finalI);
                        int active = Integer.parseInt(result.getString(5));
                        if (active == 1) {

                            isTouched = true;

                        } else {

                            s4.setChecked(true);
//                            Toast.makeText(location_settings.this, "Please Check active state!!", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }

                });

                s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isTouched) {
                            isTouched = false;
                            boolean update;
                            if (isChecked) {

                                update = db.setAir(true, state);

                            } else {

                                update = db.setAir(false, state);
                            }
                            if (update) {
                                Toast.makeText(location_settings.this, "Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(location_settings.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                s3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        Cursor result = db.locState();
                        result.moveToPosition(finalI);
                        int active = Integer.parseInt(result.getString(5));
                        if (active == 1) {

                            isTouched = true;

                        } else {

                            s3.setChecked(true);
//                            Toast.makeText(location_settings.this, "Please Check active state!!", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isTouched) {
                            isTouched = false;
                            boolean update;
                            if (isChecked) {

                                update = db.setRing(true, state);

                            } else {

                                update = db.setRing(false, state);
                            }
                            if (update) {
                                Toast.makeText(location_settings.this, "Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(location_settings.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        else {
//
//                            s3.setChecked(false);
//                        }


                    }
                });

                s2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        Cursor result = db.locState();
                        result.moveToPosition(finalI);
                        int active = Integer.parseInt(result.getString(5));
                        if (active == 1) {

                            isTouched = true;

                        } else {

                            s2.setChecked(true);
//                            Toast.makeText(location_settings.this, "Please Check active state!!", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });

                s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        if (isTouched) {
                            isTouched = false;
                            boolean update;
                            if (isChecked) {

                                update = db.setBlu(true, state);

                            } else {

                                update = db.setBlu(false, state);
                            }
                            if (update) {
                                Toast.makeText(location_settings.this, "Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(location_settings.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                defaul(state);
            }
        });
    }

    private void defaul(String state) {

            boolean updated = db.setFalse(0,state);

            if (updated){
                    s4.setChecked(false);
                    s3.setChecked(false);
                    s2.setChecked(false);
                    s1.setChecked(false);
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        }

    private void checkState(int i) {

        Cursor result = db.locState();
        result.moveToPosition(i);
        int active = Integer.parseInt(result.getString(5));
        int wifi = Integer.parseInt(result.getString(1));
        int bluetooth = Integer.parseInt(result.getString(2));
        int ring = Integer.parseInt(result.getString(3));

        if (active == 1) {

            s4.setChecked(wifi == 1);
            s3.setChecked(ring == 1);
            s2.setChecked(bluetooth == 1);

        }
        else {

            s4.setChecked(false);
            s3.setChecked(false);
            s2.setChecked(false);

        }
    }


    private void checkSettings() {

        locationList = new database(getApplicationContext()).getLocDetails();
        button_statusList = new database(getApplicationContext()).getBtnDetails();

        for (int i = 0; i < button_statusList.size(); i++) {

            String ID = button_statusList.get(i).getBTN_ID();

            if (ID.equals(state)) {

                String state = locationList.get(i).getENABLE();

                    if (String.valueOf(state).equals(String.valueOf(ACTIVE_STATE))) {

                        s1.setChecked(true);

                    } else {

                        s1.setChecked(false);
                    }
                checkState(i);
            }
            }
    }

    public void nme(){
        Cursor result = db.btnDetails();
        btn_no = btn_no-1;
        result.moveToPosition(btn_no);
        String nv = result.getString(result.getColumnIndex(database.COL_BTN_NAME));
        Toast.makeText(this, ""+nv, Toast.LENGTH_SHORT).show();
        txt.setText(nv);


    }
    public void tmp(){
        Cursor result = db.tmpass();
        result.moveToLast();
        btn_no = result.getInt(0);
        Toast.makeText(this, ""+btn_no, Toast.LENGTH_SHORT).show();
    }

}