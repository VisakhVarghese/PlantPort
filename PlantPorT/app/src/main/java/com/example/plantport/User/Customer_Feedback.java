package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plantport.Model.Query;
import com.example.plantport.Model.Users;
import com.example.plantport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.User;

import java.util.UUID;

public class Customer_Feedback extends AppCompatActivity {

    EditText rQuery,query;
    String query_id;
    Button send_query;
    String User_phone,user_name;
    DatabaseReference reference,ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__feedback);

        rQuery = findViewById(R.id.customer_reason);
        query = findViewById(R.id.customer_query);
        send_query = findViewById(R.id.send_query);

        reference = FirebaseDatabase.getInstance().getReference("Queries");
        ref = FirebaseDatabase.getInstance().getReference("Customer");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    Users users = dataSnapshot.getValue(Users.class);
                    assert users != null;
                    User_phone = users.getPhone();
                    user_name = users.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                query_id = UUID.randomUUID().toString();
                Query feedback = new Query(rQuery.getText().toString(),query.getText().toString(),query_id,userId,User_phone,user_name);
                reference.child(query_id).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {

                            Toast.makeText(Customer_Feedback.this, "Query Send!", Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(Customer_Feedback.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),Customer_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}