package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantport.Holders.Customer_PlantViewHolder;
import com.example.plantport.Model.PlantData;
import com.example.plantport.Model.Users;
import com.example.plantport.R;
import com.example.plantport.Selection_Activity;
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

import java.util.Objects;

public class Customer_PlantView extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseReference reference;
    String query;
    String MenuId,Owner_Id;
    RecyclerView recyclerView;
    String Phone,name,user_name,phone;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    FirebaseRecyclerAdapter<PlantData, Customer_PlantViewHolder>adapter;
    FirebaseRecyclerAdapter<PlantData, Customer_PlantViewHolder>adapter1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__plant_view);

        Toolbar toolbar = findViewById(R.id.toolbar5);
        toolbar.setTitle("Plants");
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_plantview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);
                assert users != null;
                user_name = users.getName();
                phone = users.getPhone();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Cart.class);
                intent.putExtra("UserName",user_name);
                intent.putExtra("Phone",Phone);
                startActivity(intent);
            }
        });

        MenuId = getIntent().getStringExtra("MenuID");
        Owner_Id = getIntent().getStringExtra("OwnerId");

        reference = FirebaseDatabase.getInstance().getReference("PlantDetails").child(Owner_Id).child(MenuId);

        FirebaseRecyclerOptions<PlantData>options =new  FirebaseRecyclerOptions.Builder<PlantData>().setQuery(reference,PlantData.class).build();
        adapter = new FirebaseRecyclerAdapter<PlantData, Customer_PlantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Customer_PlantViewHolder customer_plantViewHolder, int i, @NonNull PlantData plantData) {

                customer_plantViewHolder.PlantName.setText(plantData.getPlant_Name());
                customer_plantViewHolder.Price.setText("Price" +plantData.getPrice()+ "Rs");
                Picasso.get().load(plantData.getImage_URL()).into(customer_plantViewHolder.imageView);

                customer_plantViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final CharSequence[] str = {"Buy","Cancel"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Customer_PlantView.this);
                        dialog.setTitle("Options");
                        dialog.setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (str[i].equals("Buy")){

                                    String OwnerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer").child(OwnerId);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Users users = snapshot.getValue(Users.class);
                                            assert users != null;
                                            Phone = users.getPhone();
                                            name = users.getName();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent = new Intent(getApplicationContext(), Customer_PlantDetails.class);
                                            intent.putExtra("Description",plantData.getDescription());
                                            intent.putExtra("Price",plantData.getPrice());
                                            intent.putExtra("PlantName",plantData.getPlant_Name());
                                            intent.putExtra("PlantImage",plantData.getImage_URL());
                                            intent.putExtra("plantId",plantData.getRandomUID());
                                            intent.putExtra("UserName",name);
                                            intent.putExtra("Phone",phone);
                                            intent.putExtra("OwnerId",Owner_Id);
                                            intent.putExtra("MenuId", MenuId);
                                            intent.putExtra("UserName",user_name);
                                            intent.putExtra("Data","PlantData");
                                    startActivity(intent);
                                    finish();
                                }
                                else if (str[i].equals("Cancel")){

                                    dialogInterface.dismiss();
                                }

                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                        return true;

                    }
                });
                customer_plantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getApplicationContext(), Customer_VideoList.class);
                        i.putExtra("MenuId",plantData.getMenu_Id());
                        i.putExtra("OwnerId",plantData.getOwnerId());
                        i.putExtra("PlantId",plantData.getRandomUID());
                        startActivity(i);
                        finish();

                    }
                });

            }

            @NonNull
            @Override
            public Customer_PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.owner_menuplants,parent,false);
                return new Customer_PlantViewHolder(v);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Customer_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        query = query.toLowerCase();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("PlantDetails").child(Owner_Id).child(MenuId);

        Query sQuery = reference1.orderByChild("plant_Name").startAt(query).endAt(query+ "\uf8ff");
        FirebaseRecyclerOptions<PlantData>options =new  FirebaseRecyclerOptions.Builder<PlantData>().setQuery(sQuery,PlantData.class).build();
        adapter1 = new FirebaseRecyclerAdapter<PlantData, Customer_PlantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Customer_PlantViewHolder customer_plantViewHolder, int i, @NonNull PlantData plantData) {

                customer_plantViewHolder.PlantName.setText(plantData.getPlant_Name());
                customer_plantViewHolder.Price.setText("Price" +plantData.getPrice()+ "Rs");
                Picasso.get().load(plantData.getImage_URL()).into(customer_plantViewHolder.imageView);

                customer_plantViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final CharSequence[] str = {"Buy","Cancel"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Customer_PlantView.this);
                        dialog.setTitle("Options");
                        dialog.setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (str[i].equals("Buy")){

                                    String OwnerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer").child(OwnerId);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Users users = snapshot.getValue(Users.class);
                                            assert users != null;
                                            Phone = users.getPhone();
                                            name = users.getName();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent = new Intent(getApplicationContext(), Customer_PlantDetails.class);
                                    intent.putExtra("Description",plantData.getDescription());
                                    intent.putExtra("Price",plantData.getPrice());
                                    intent.putExtra("PlantName",plantData.getPlant_Name());
                                    intent.putExtra("PlantImage",plantData.getImage_URL());
                                    intent.putExtra("plantId",plantData.getRandomUID());
                                    intent.putExtra("UserName",name);
                                    intent.putExtra("Phone",phone);
                                    intent.putExtra("OwnerId",Owner_Id);
                                    intent.putExtra("MenuId", MenuId);
                                    intent.putExtra("UserName",user_name);
                                    intent.putExtra("Data","PlantData");
                                    startActivity(intent);
                                    finish();
                                }
                                else if (str[i].equals("Cancel")){

                                    dialogInterface.dismiss();
                                }

                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                        return true;

                    }
                });
                customer_plantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getApplicationContext(), Customer_VideoList.class);
                        i.putExtra("MenuId",plantData.getMenu_Id());
                        i.putExtra("OwnerId",plantData.getOwnerId());
                        i.putExtra("PlantId",plantData.getRandomUID());
                        startActivity(i);
                        finish();

                    }
                });

            }

            @NonNull
            @Override
            public Customer_PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.owner_menuplants,parent,false);
                return new Customer_PlantViewHolder(v);
            }
        };

        recyclerView.setAdapter(adapter1);
        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {

        query = newText.toLowerCase();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("PlantDetails").child(Owner_Id).child(MenuId);

        Query sQuery = reference1.orderByChild("plant_Name").startAt(query).endAt(query+ "\uf8ff");
        FirebaseRecyclerOptions<PlantData>options =new  FirebaseRecyclerOptions.Builder<PlantData>().setQuery(sQuery,PlantData.class).build();
        adapter1 = new FirebaseRecyclerAdapter<PlantData, Customer_PlantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Customer_PlantViewHolder customer_plantViewHolder, int i, @NonNull PlantData plantData) {

                customer_plantViewHolder.PlantName.setText(plantData.getPlant_Name());
                customer_plantViewHolder.Price.setText("Price" +plantData.getPrice()+ "Rs");
                Picasso.get().load(plantData.getImage_URL()).into(customer_plantViewHolder.imageView);

                customer_plantViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final CharSequence[] str = {"Buy","Cancel"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Customer_PlantView.this);
                        dialog.setTitle("Options");
                        dialog.setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (str[i].equals("Buy")){

                                    String OwnerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer").child(OwnerId);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Users users = snapshot.getValue(Users.class);
                                            assert users != null;
                                            Phone = users.getPhone();
                                            name = users.getName();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent = new Intent(getApplicationContext(), Customer_PlantDetails.class);
                                    intent.putExtra("Description",plantData.getDescription());
                                    intent.putExtra("Price",plantData.getPrice());
                                    intent.putExtra("PlantName",plantData.getPlant_Name());
                                    intent.putExtra("PlantImage",plantData.getImage_URL());
                                    intent.putExtra("plantId",plantData.getRandomUID());
                                    intent.putExtra("UserName",name);
                                    intent.putExtra("Phone",phone);
                                    intent.putExtra("OwnerId",Owner_Id);
                                    intent.putExtra("MenuId", MenuId);
                                    intent.putExtra("UserName",user_name);
                                    intent.putExtra("Data","PlantData");
                                    startActivity(intent);
                                    finish();
                                }
                                else if (str[i].equals("Cancel")){

                                    dialogInterface.dismiss();
                                }

                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                        return true;

                    }
                });
                customer_plantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getApplicationContext(), Customer_VideoList.class);
                        i.putExtra("MenuId",plantData.getMenu_Id());
                        i.putExtra("OwnerId",plantData.getOwnerId());
                        i.putExtra("PlantId",plantData.getRandomUID());
                        startActivity(i);
                        finish();

                    }
                });

            }

            @NonNull
            @Override
            public Customer_PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.owner_menuplants,parent,false);
                return new Customer_PlantViewHolder(v);
            }
        };

        recyclerView.setAdapter(adapter1);
        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        return false;
    }
}