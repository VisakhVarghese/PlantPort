    package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.plantport.Database.Database;
import com.example.plantport.Fragments.OwnerOrderfragment;
import com.example.plantport.Holders.Cart_Adapter;
import com.example.plantport.Model.Order;
import com.example.plantport.Model.Requests;
import com.example.plantport.R;
import com.example.plantport.Service.ListenerOrderStatus;
import com.example.plantport.Service.ListenerOrders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

    public class Cart extends AppCompatActivity {

    Button btnPlace;
    String UserId;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    DatabaseReference reff,ref;
    TextView txtTotalPrice;
    List<Order>list = new ArrayList<>();
    List<Order>getList=new ArrayList<>();
    String user_name,phone,OwnerId;
    Cart_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        toolbar.setTitle("Cart");
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        user_name = getIntent().getStringExtra("UserName");
        phone = getIntent().getStringExtra("Phone");


        txtTotalPrice = (TextView) findViewById(R.id.total_price);
        btnPlace = findViewById(R.id.place_order);
        database = FirebaseDatabase.getInstance();
        reff = database.getReference("Requests");
        ref = database.getReference("Orders");

        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = (RecyclerView) findViewById(R.id.Recycler_ListCart);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list = new Database(getApplicationContext()).getCarts();
                for (int i = 0 ; i < list.size() ; i ++){

                    if (list.get(i).getUserId().equals(UserId)){

                        OwnerId = list.get(i).getOwnerId();
                    }
                }
                String total = txtTotalPrice.getText().toString().trim();
                String OrderId = String.valueOf(System.currentTimeMillis());
//                Requests requests = new Requests(list,user_name,phone,txtTotalPrice.getText().toString());
//                reff.child(String.valueOf(System.currentTimeMillis())).setValue(requests);

                Requests requests = new Requests(getList,user_name,phone,total,UserId,OrderId);
                ref.child(OwnerId).child(OrderId).setValue(requests);
                Listener(OwnerId,OrderId);

                new Database(getBaseContext()).clearCart(UserId);
                Toast.makeText(Cart.this, "Thank you,Order Placed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        loadListPlants();
    }

        private void Listener(String ownerId,String orderId) {

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ref.child(ownerId).child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Requests requests = snapshot.getValue(Requests.class);
                        assert requests != null;
                        showNotification(requests.getOrderId(),requests);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

        private void showNotification(String key, Requests requests) {


            Intent intent = new Intent(Cart.this, OwnerOrderfragment.class);
//            intent.putExtra("customerPhone",requests.getMob());    // we need put user here
            PendingIntent contentIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setAutoCancel(true).
                    setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("PlantPort")
                    .setContentInfo("Your order was updated")
                    .setContentText("New Order Placed by "+requests.getPlant_name()+".OrderId is #"+key+"")
                    .setContentIntent(contentIntent)
                    .setContentInfo("Info")
                    .setSmallIcon(R.mipmap.ic_launcher);


            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Linux";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Message",
                        NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            manager.notify(0,builder.build());

        }

        private void loadListPlants() {

//        list.clear();
        list = new Database(this).getCarts();
        for (int i = 0 ; i < list.size() ; i++){

            if (list.get(i).getUserId().equals(UserId)){

                getList.add(list.get(i));
            }
        }
        adapter = new Cart_Adapter(getList,this,UserId);
        recyclerView.setAdapter(adapter);

       int total = 0 ;
       for (Order order : getList){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
           Locale locale = new Locale("en","US");
           NumberFormat format = NumberFormat.getCurrencyInstance(locale);

           txtTotalPrice.setText(format.format(total));
       }
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