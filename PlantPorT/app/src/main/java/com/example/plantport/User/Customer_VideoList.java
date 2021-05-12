package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantport.Holders.Video_Adapter;
import com.example.plantport.Model.UpdateVideo;
import com.example.plantport.R;
import com.example.plantport.Selection_Activity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Customer_VideoList extends AppCompatActivity implements SearchView.OnQueryTextListener{


    String MenuId,OwnerId,PlantId;
    RecyclerView recyclerView;
    String query;
    DatabaseReference ref;
    StorageReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__video_list);

        Toolbar toolbar = findViewById(R.id.toolbar_videolist);
        toolbar.setTitle("Videos");
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        MenuId = getIntent().getStringExtra("MenuId");
        OwnerId = getIntent().getStringExtra("OwnerId");
        PlantId = getIntent().getStringExtra("PlantId");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_cVideo);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


//        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Plant Videos").child(OwnerId).child(MenuId).child(PlantId);
        sRef = FirebaseStorage.getInstance().getReference("Plant Videos");
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),Customer_Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("MenuID",MenuId);
        i.putExtra("OwnerId",OwnerId);
        startActivity(i);
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchmenu_user, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item1);
        getMenuInflater().inflate(R.menu.logout, menu);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search....");
        SearchManager manager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(this.getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            logout();
            return true;
        }
        if (item.getItemId() == R.id.check_staus) {
            startActivity(new Intent(this, Customer_OrderStatus.class));
            finish();
        }
        if (item.getItemId() == R.id.contact_Us) {

            startActivity(new Intent(getApplicationContext(), Customer_Feedback.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Selection_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

                video_adapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Customer_VideoList.this, FullScreenView.class);
                        intent.putExtra("VideoURL", updateVideo.getVideoUrl());
                        intent.putExtra("Date", updateVideo.getDate());
                        intent.putExtra("Description", updateVideo.getDescription());
                        startActivity(intent);

                    }
                });

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        query = newText.toLowerCase();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Plant Videos").
                child(OwnerId).child(MenuId).child(PlantId);

        Query sQuery = reference1.orderByChild("date").startAt(query).endAt(query+ "\uf8ff");

        FirebaseRecyclerOptions<UpdateVideo> options = new FirebaseRecyclerOptions.Builder<UpdateVideo>()
                .setQuery(sQuery, UpdateVideo.class).build();
        FirebaseRecyclerAdapter<UpdateVideo, Video_Adapter> adapter = new FirebaseRecyclerAdapter<UpdateVideo, Video_Adapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Video_Adapter video_adapter, int i, @NonNull UpdateVideo updateVideo) {

                video_adapter.setVideo(getApplication(), updateVideo.getVideoUrl());
                video_adapter.date.setText(updateVideo.getDate());

                video_adapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Customer_VideoList.this, FullScreenView.class);
                        intent.putExtra("VideoURL", updateVideo.getVideoUrl());
                        intent.putExtra("Date", updateVideo.getDate());
                        intent.putExtra("Description", updateVideo.getDescription());
                        startActivity(intent);

                    }
                });

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

        return false;
    }
}