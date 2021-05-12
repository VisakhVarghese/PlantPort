package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Holders.Video_Adapter;
import com.example.plantport.Model.UpdateVideo;
//import com.example.plantport4.Holder.OwnerVideoAdapterList;
//import com.example.plantport4.OwnerPlants.UpdateVideo;
import com.example.plantport.User.Customer_Home;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Owner_Videolist extends AppCompatActivity {

    String RandomId;

    RecyclerView recyclerView;

    DatabaseReference ref;

    StorageReference sRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__videolist);

        RandomId = getIntent().getStringExtra("RandomId");

        recyclerView = (RecyclerView) findViewById(R.id.videolist_recylcer);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Plant Videos").child(str).child(RandomId);
        sRef = FirebaseStorage.getInstance().getReference("Plant Videos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.update, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.update) {
          return true;
        }
         return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<UpdateVideo> options = new FirebaseRecyclerOptions.Builder<UpdateVideo>()
                .setQuery(ref, UpdateVideo.class).build();
        FirebaseRecyclerAdapter<UpdateVideo, Video_Adapter> adapter = new FirebaseRecyclerAdapter<UpdateVideo, Video_Adapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Video_Adapter video_adapter, int i, @NonNull UpdateVideo updateVideo) {

                video_adapter.setVideo(getApplication(), updateVideo.getVideoUrl());
                video_adapter.date.setText(updateVideo.getDate());

            }

            @NonNull
            @Override
            public Video_Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_menulist, parent, false);
                return new Video_Adapter(v);
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}