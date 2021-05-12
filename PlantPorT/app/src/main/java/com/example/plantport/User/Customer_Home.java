package com.example.plantport.User;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.DataUrlLoader;
import com.example.plantport.Holders.Customer_InnerAdapter;
import com.example.plantport.Holders.Customer_OuterAdapter;
import com.example.plantport.Holders.OwnerMenuAdapter;
import com.example.plantport.Model.Owner;
import com.example.plantport.Model.PlantData;
import com.example.plantport.Model.Users;
import com.example.plantport.OwnerPlantView;
import com.example.plantport.R;
import com.example.plantport.Selection_Activity;
import com.example.plantport.Service.ListenerOrderStatus;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer_Home extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseReference reference;
    String UserName, Phone;
    List<Owner> ownerList;
    List<PlantData> plantDataList;
    RecyclerView recyclerView;
    DatabaseReference reff;
    String query;
    List<Owner> list;
    List<Owner> newList = new ArrayList<>();
    FloatingActionButton Fbtn;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Owner, Customer_OuterAdapter> adapter;
    FirebaseRecyclerAdapter<com.example.plantport.Model.Menu, Customer_InnerAdapter> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__home);

        Toolbar toolbar = findViewById(R.id.toolbar_customer);
        toolbar.setTitle("Home");
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        ownerList = new ArrayList<>();
        plantDataList = new ArrayList<>();
        list = new ArrayList<>();

        Fbtn = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView = findViewById(R.id.rv_outer);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        reference = firebaseDatabase.getReference("Owner");
        reff = firebaseDatabase.getReference().child("MenuDetails");

        DatabaseReference reff = firebaseDatabase.getReference("Customer").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reff.addValueEventListener(new ValueEventListener() {

            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);
                assert users != null;
                UserName = users.getName();
                Phone = users.getPhone();

                loadData();
            }
        });
        Fbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {

                Intent intent = new Intent(Customer_Home.this, Cart.class);
                intent.putExtra("UserName", UserName);
                intent.putExtra("Phone", Phone);
                startActivity(intent);
            }
        });
    }

    private void loadData() {

        FirebaseRecyclerOptions<Owner> options = new FirebaseRecyclerOptions.Builder<Owner>().setQuery(reference, Owner.class).build();
        adapter = new FirebaseRecyclerAdapter<Owner, Customer_OuterAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Customer_OuterAdapter customer_outerAdapter, int i, @NonNull Owner owner) {

                customer_outerAdapter.shopeName.setText(owner.getFirst_Name());


                FirebaseRecyclerOptions<com.example.plantport.Model.Menu> options1 = new FirebaseRecyclerOptions.Builder<com.example.plantport.Model.Menu>()
                        .setQuery(reff.child(owner.getOwnerId()), com.example.plantport.Model.Menu.class).build();

                adapter1 = new FirebaseRecyclerAdapter<com.example.plantport.Model.Menu, Customer_InnerAdapter>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull Customer_InnerAdapter customer_innerAdapter, int i, @NonNull com.example.plantport.Model.Menu menudata) {

                        customer_innerAdapter.plantName.setText(menudata.getMenu_Name());
                        Picasso.get().load(menudata.getImage_URL()).into(customer_innerAdapter.imageView);

                        customer_innerAdapter.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(Customer_Home.this, Customer_PlantView.class);
                                i.putExtra("MenuID", menudata.getRandomId());
                                i.putExtra("OwnerId", menudata.getUserId());
                                startActivity(i);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Customer_InnerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_view_home, parent, false);
                        return new Customer_InnerAdapter(v);
                    }
                };

                adapter1.startListening();
                adapter1.notifyDataSetChanged();
                customer_outerAdapter.rv_outer.setAdapter(adapter1);
            }

            @NonNull
            @Override
            public Customer_OuterAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.outer_rv_customerhome, parent, false);

                return new Customer_OuterAdapter(v);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Selection_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.searchmenu_user, menu);
        getMenuInflater().inflate(R.menu.logout, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_item1);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search....");
//        SearchManager manager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(manager.getSearchableInfo(this.getComponentName()));
//        searchView.setOnQueryTextListener(this);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.logout) {

            logout();
            return true;
        }
        if (menuItem.getItemId() == R.id.check_staus) {
            startActivity(new Intent(this, Customer_OrderStatus.class));
            finish();
        }
        if (menuItem.getItemId() == R.id.contact_Us) {

            startActivity(new Intent(getApplicationContext(), Customer_Feedback.class));
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        query = newText.toLowerCase();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Owner");

        Query sQuery = reference1.orderByChild("First_Name").startAt(query).endAt(query+ "\uf8ff");
        reference1.keepSynced(true);//

        FirebaseRecyclerOptions<Owner> options = new FirebaseRecyclerOptions.Builder<Owner>().setQuery(sQuery, Owner.class).build();
        adapter = new FirebaseRecyclerAdapter<Owner, Customer_OuterAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Customer_OuterAdapter customer_outerAdapter, int i, @NonNull Owner owner) {

                customer_outerAdapter.shopeName.setText(owner.getFirst_Name());


                FirebaseRecyclerOptions<com.example.plantport.Model.Menu> options1 = new FirebaseRecyclerOptions.Builder<com.example.plantport.Model.Menu>()
                        .setQuery(reff.child(owner.getOwnerId()), com.example.plantport.Model.Menu.class).build();

                adapter1 = new FirebaseRecyclerAdapter<com.example.plantport.Model.Menu, Customer_InnerAdapter>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull Customer_InnerAdapter customer_innerAdapter, int i, @NonNull com.example.plantport.Model.Menu menudata) {

                        customer_innerAdapter.plantName.setText(menudata.getMenu_Name());
                        Picasso.get().load(menudata.getImage_URL()).into(customer_innerAdapter.imageView);

                        customer_innerAdapter.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(Customer_Home.this, Customer_PlantView.class);
                                i.putExtra("MenuID", menudata.getRandomId());
                                i.putExtra("OwnerId", menudata.getUserId());
                                startActivity(i);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Customer_InnerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_view_home, parent, false);
                        return new Customer_InnerAdapter(v);
                    }
                };

                adapter1.startListening();
                adapter1.notifyDataSetChanged();
                customer_outerAdapter.rv_outer.setAdapter(adapter1);
            }

            @NonNull
            @Override
            public Customer_OuterAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.outer_rv_customerhome, parent, false);

                return new Customer_OuterAdapter(v);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
//        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        list = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Owner");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Owner owner = dataSnapshot.getValue(Owner.class);
//                    list.add(owner);
//
//                    for (Owner owner1 : list) {
//
//                        if (owner1.getFirst_Name().toLowerCase().contains(query)) {
//
//                            newList.add(owner1);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        adapter = new FirebaseRecyclerAdapter(Customer_Home.this,newList);
//        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return true;
    }
}
