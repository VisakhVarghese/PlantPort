package com.example.smartmode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SetLocation extends AppCompatActivity {

    database addDb;
    ImageView usrbtn;
    database db;
    public int btno;
    ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
    TextView txt1,txt2,txt3,txt4,txt5,txt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        addDb = new database(this);
        getSupportActionBar().hide();
        btno=0;
        db = new database(SetLocation.this);
        usrbtn = findViewById(R.id.userbtn);

        btn1 = findViewById(R.id.imageButton1);
        btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton3);
        btn4 = findViewById(R.id.imageButton4);
        btn5 = findViewById(R.id.imageButton5);
        btn6 = findViewById(R.id.imageButton6);

        txt1 = findViewById(R.id.textView1);
        txt2 = findViewById(R.id.textView2);
        txt3 = findViewById(R.id.textView3);
        txt4 = findViewById(R.id.textView4);
        txt5 = findViewById(R.id.textView5);
        txt6 = findViewById(R.id.textView6);



        btnlabl();


       /* btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetLocation.this,Add_Location.class);
                startActivity(intent);
            }
        });
*/
      btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor result = db.btnV();
                result.moveToPosition(0);
                int v =Integer.parseInt(result.getString(1));
                //int v = result.getInt(result.getColumnIndex(database.COL_BTN_CREATE));
                Toast.makeText(SetLocation.this, "btn select "+v, Toast.LENGTH_SHORT).show();
                if (v == 0) {
                    btno = 1;
                    String btn = String.valueOf(1);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(1);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
                //Toast.makeText(SetLocation.this, "aaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();


               // Intent intent = new Intent(SetLocation.this,Add_Location.class);
                //startActivity(intent);
            }
        });





        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = db.btnV();
                result.moveToPosition(1);
                int v =Integer.parseInt(result.getString(1));
                if (v == 0) {
                    btno = 2;
                    String btn = String.valueOf(2);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(2);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = db.btnV();
                result.moveToPosition(2);
                int v =Integer.parseInt(result.getString(1));
                if (v == 0) {
                    btno = 3;
                    String btn = String.valueOf(3);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(3);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = db.btnV();
                result.moveToPosition(3);
                int v =Integer.parseInt(result.getString(1));
                if (v == 0) {
                    btno = 4;
                    String btn = String.valueOf(4);
//                    db.setEnable(v,btn);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(4);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = db.btnV();
                result.moveToPosition(4);
                int v =Integer.parseInt(result.getString(1));
                if (v == 0) {
                    btno = 5;
                    String btn = String.valueOf(5);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(5);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = db.btnV();
                result.moveToPosition(5);
                int v =Integer.parseInt(result.getString(1));
                if (v == 0) {
                    btno = 6;
                    String btn = String.valueOf(6);
                    tpvalu();
                    Intent intent = new Intent(SetLocation.this, Add_Location.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                    Toast.makeText(SetLocation.this, "Add", Toast.LENGTH_SHORT).show();

                }
                else if (v == 1) {
                    String btn = String.valueOf(6);
                    Intent intent = new Intent(SetLocation.this, location_settings.class);
                    intent.putExtra("State",btn);
                    startActivity(intent);
                }
                db.close();
            }
        });

        usrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetLocation.this,User.class);
                startActivity(intent);
            }
        });




    }
    public void btnlabl(){
        Cursor result = db.btnDetails();
        int ct = result.getCount();
        if(ct!=0){
            result.moveToFirst();
            do {
               // result.moveToPosition(i);
                int n = result.getInt(0);
                int nn = result.getInt(1);
                String nv = result.getString(2);
                if (n == 1) {
                    if(nn == 1)
                    {
                        btn1.setImageResource(R.drawable.loctn);
                        txt1.setText(nv);
                        // Toast.makeText(this, "bbbbbbbbbbbb", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        btn1.setImageResource(R.drawable.add_location);
                        txt1.setText("Add new");
                    }
                }
                else if (n == 2) {
                    if (nn == 1) {
                        btn2.setImageResource(R.drawable.loctn);
                        txt2.setText(nv);
                    }
                    else{
                        btn2.setImageResource(R.drawable.add_location);
                        txt2.setText("Add new");
                    }
                }
                else if (n == 3) {
                    if (nn == 1) {
                        btn3.setImageResource(R.drawable.loctn);
                        txt3.setText(nv);
                    }
                    else{
                        btn3.setImageResource(R.drawable.add_location);
                        txt3.setText("Add new");
                    }
                }
                else if (n == 4) {
                    if (nn == 1) {
                        btn4.setImageResource(R.drawable.loctn);
                        txt4.setText(nv);
                    }
                    else{
                        btn4.setImageResource(R.drawable.add_location);
                        txt4.setText("Add new");
                    }
                }
                else if (n == 5) {
                    if (nn == 1) {
                        btn5.setImageResource(R.drawable.loctn);
                        txt5.setText(nv);
                    }
                    else{
                        btn5.setImageResource(R.drawable.add_location);
                        txt5.setText("Add new");
                    }
                }
                else if (n == 6) {
                    if (nn == 1) {
                        btn6.setImageResource(R.drawable.loctn);
                        txt6.setText(nv);
                    }
                    else{
                        btn6.setImageResource(R.drawable.add_location);
                        txt6.setText("Add new");
                    }
                }
                result.moveToNext();
            }while(!result.isAfterLast());

        }
    }

    public void tpvalu(){
        db.del();
        Toast.makeText(this, "------"+btno, Toast.LENGTH_SHORT).show();
        boolean updated = db.tmp(btno);
        if(updated==true){
            Toast.makeText(this, "btn added ", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "failed at ", Toast.LENGTH_LONG).show();
    }

}