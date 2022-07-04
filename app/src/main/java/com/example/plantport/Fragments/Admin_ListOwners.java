package com.example.plantport.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Owner;
import com.example.plantport.Owner_Home;
import com.example.plantport.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Admin_ListOwners extends Fragment {

    Toolbar toolbar;
    ListView listView;
    DatabaseReference reference ;
    FirebaseListAdapter<Owner>adapter;
    Owner owner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_listowners,null);
        Objects.requireNonNull(getActivity()).setTitle("Shops");

        toolbar = v.findViewById(R.id.toolbar_shop);
        toolbar.setTitle("Shops");
        toolbar.setTitleTextAppearance(getActivity(),R.style.ToolbarTextAppearance);

       listView = v.findViewById(R.id.list_view);

       reference = FirebaseDatabase.getInstance().getReference("Owner");
       loadData();
        return v;
    }

    private void loadData() {

        FirebaseListOptions<Owner>options = new FirebaseListOptions.Builder<Owner>().
                setLayout(R.layout.shop_listview).setQuery(reference,Owner.class).build();
        adapter = new FirebaseListAdapter<Owner>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Owner model, int position) {

                TextView shopename = v.findViewById(R.id.shopename);
                TextView address = v.findViewById(R.id.address);
                TextView phoneno = v.findViewById(R.id.phoneno);

                owner =(Owner) model;
                shopename.setText("Nursery :"+""+owner.getFirst_Name());
                address.setText("Address :"+""+owner.getOffice_Area());
                phoneno.setText("Phone   :"+""+owner.getPhone());



            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
