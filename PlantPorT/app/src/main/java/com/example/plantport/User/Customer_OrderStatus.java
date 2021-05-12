package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.plantport.Holders.Customer_OuterAdapter;
import com.example.plantport.Holders.Customer_StatusAdapter;
import com.example.plantport.Model.Requests;
import com.example.plantport.Model.Users;
import com.example.plantport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer_OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private Customer_StatusAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference reff,reference;
    String Customer_Id,Phone_No;
    List<Requests>requestsList= new ArrayList<>();
    List<Requests>list = new ArrayList<>();

//    FirebaseRecyclerAdapter<Requests, Customer_StatusAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__order_status);

        recyclerView = findViewById(R.id.recycler_status);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar_cStatus);
        toolbar.setTitle("Order Status");
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        reff = database.getReference("Orders");

        Customer_Id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Requests requests = dataSnapshot1.getValue(Requests.class);
                        assert requests != null;
                        requestsList.add(requests);

                    }
                }
                loadAdapter(requestsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadAdapter(List<Requests> requestsList) {

        for (int i = 0 ; i < requestsList.size() ; i ++){

            if (requestsList.get(i).getUserId().equals(Customer_Id)){

                list.add(requestsList.get(i));
            }
        }
        adapter = new Customer_StatusAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Customer_Home.class));
        finish();
    }
}