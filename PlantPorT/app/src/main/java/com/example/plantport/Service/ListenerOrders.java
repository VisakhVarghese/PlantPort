package com.example.plantport.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.plantport.Fragments.OwnerOrderfragment;
import com.example.plantport.Model.Requests;
import com.example.plantport.R;
import com.example.plantport.User.Customer_OrderStatus;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Provider;

public class ListenerOrders extends Service implements ChildEventListener {

    FirebaseDatabase database;
    String OwnerId;
    DatabaseReference reference;

    public ListenerOrders() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Orders");
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

        OwnerId = intent.getStringExtra("Id");
        assert OwnerId != null;
        reference.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        //Trigger Here
        Requests requests = snapshot.getValue(Requests.class);
        assert requests != null;
        showNotification(snapshot.getKey(),requests);

    }

    private void showNotification(String key, Requests requests) {


        Intent intent = new Intent(getBaseContext(), OwnerOrderfragment.class);
        intent.putExtra("customerPhone",requests.getMob());    // we need put user here
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("PlantPort")
                .setContentInfo("Your order was updated")
                .setContentText("Order #"+key+" is Placed")
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
}
