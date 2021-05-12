package com.example.plantport.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Holders.OrderDetailsHolder;
import com.example.plantport.Holders.Owner_StatusAdapter;
import com.example.plantport.Model.Order;
import com.example.plantport.Model.Requests;
import com.example.plantport.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class OwnerOrderfragment extends Fragment {

    RecyclerView recyclerView,recyclerView1;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference reference;
    OrderDetailsHolder adapter1;
    FirebaseRecyclerAdapter<Requests, Owner_StatusAdapter>adapter;
    MaterialSpinner spinner;
    TextView plantname,quantity,price,Total;
    List<Order>orderList = new ArrayList<>();
    Toolbar toolbar;

    String Id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customerorders, null);
        setHasOptionsMenu(true);

        toolbar = view.findViewById(R.id.toolbar_order);
        toolbar.setTitle("Orders");
        toolbar.setTitleTextAppearance(getActivity(),R.style.ToolbarTextAppearance);

        recyclerView = view.findViewById(R.id.recycler_order);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("Orders").child(Id);

        loadOrders();
        return view;

    }

    private void loadOrders() {

        FirebaseRecyclerOptions<Requests>options = new FirebaseRecyclerOptions.Builder<Requests>().
                setQuery(reference,Requests.class).build();
        adapter = new FirebaseRecyclerAdapter<Requests, Owner_StatusAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Owner_StatusAdapter owner_statusAdapter, int position, @NonNull Requests requests) {

                owner_statusAdapter.Order_id.setText("#"+adapter.getRef(position).getKey());
                owner_statusAdapter.phone.setText(requests.getMob());
                owner_statusAdapter.status.setText(convertCodeStatus(requests.getStatus()));

                owner_statusAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String[] str = {"Details","Update","Delete","Cancel"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Select Actions");
                        dialog.setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (str[i].equals("Details")){

                                    showDeatils(requests.getOrderList(),adapter.getItem(position));
                                }

                                if (str[i].equals("Update"))

                                    showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));

                                if (str[i].equals("Delete"))

                                    deleteOrder(adapter.getRef(position).getKey());

                                if (str[i].equals("Cancel"))

                                    dialogInterface.getClass();
                            }
                        });
                        AlertDialog dialog1 = dialog.create();
                        dialog1.show();
                    }
                });

            }

            @NonNull
            @Override
            public Owner_StatusAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getActivity()).inflate(R.layout.ownerstatus_layout,parent,false);
                return new Owner_StatusAdapter(v);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void showDeatils(List<Order> orderList, Requests item) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Order Details");

        LayoutInflater layoutInflater = getLayoutInflater();
        final View v = layoutInflater.inflate(R.layout.demo,null);

        recyclerView1 = v.findViewById(R.id.demo);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter1 = new OrderDetailsHolder(getActivity(),orderList);
        recyclerView1.setAdapter(adapter1);


//        for (int i = 0 ; i <orderList.size() ; i++){

//            plantname.setText("Plant      :"+" "+orderList.get(i).getPlantName());
//            quantity.setText("Quantity :"+" "+orderList.get(i).getQuantity());
//            price.setText("Price      :"+" "+orderList.get(i).getPrice());
//            Total.setText("Total      :"+" "+item.getAmount());
//        }
        builder.setView(v);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getIntent().equals("Update")){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if (item.getIntent().equals("Delete")){
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {

        reference.child(key).removeValue();
    }


    private void showUpdateDialog(String key, Requests item) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Order");
        builder.setMessage("Please change status");

        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.update_order_layout,null);

        spinner = v.findViewById(R.id.status_spinner);
        spinner.setItems("Placed","Accepted","Ready To Deliver");

        builder.setView(v);

        final String localKey = key;

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                reference.child(localKey).setValue(item);

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        builder.show();

    }

    private String convertCodeStatus(String status) {

        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Accepted";
        else
            return "Ready To Deliver";
    }

}
