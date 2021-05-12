package com.example.plantport.Holders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.plantport.Database.Database;
import com.example.plantport.Model.Order;
import com.example.plantport.Model.PlantData;
import com.example.plantport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.ViewHolder> {

    private final List<Order>orderList;
    private final Context context;
    private final String userId;
    DatabaseReference reference;
    Database db;


    public Cart_Adapter(List<Order> orderList, Context context,String UserId) {

        this.orderList = orderList;
        this.context = context;
        this.userId = UserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.cart_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        for (int i =0;i <orderList.size();i++) {

            if (orderList.get(i).getUserId().equals(userId)) {

                TextDrawable textDrawable = TextDrawable.builder().buildRound("" + orderList.get(position), Color.RED);
                holder.image_view_count.setImageDrawable(textDrawable);

                Locale locale = new Locale("en", "US");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                int cart_price = (Integer.parseInt(orderList.get(position).getPrice())) * (Integer.parseInt(orderList.get(position).getQuantity()));
                holder.price.setText(format.format(cart_price));
                holder.eBtn.setNumber(orderList.get(position).getQuantity());
                holder.eBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                        db.setUpdate(newValue,orderList.get(position).getID());
//                if (update == true){
//                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
//                }
                    }
                });
                holder.plant_name.setText(orderList.get(position).getPlantName());
            }
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final CharSequence[] str = {"Delete","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Action");
                builder.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (str[i].equals("Delete")){

                            new Database(context).cart(orderList.get(position).getPlantId());
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            reference = FirebaseDatabase.getInstance().getReference().child("PlantDetails").child(orderList.get(position).getOwnerId())
                                    .child(orderList.get(position).getMenuId()).child(orderList.get(position).getPlantId());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    PlantData plantData = snapshot.getValue(PlantData.class);
                                    assert plantData != null;
                                    int Qun = Integer.parseInt(plantData.getQuantity());
                                    Integer add = Qun+Integer.parseInt(orderList.get(position).getQuantity());
                                    plantData.setQuantity(add.toString());
                                    reference.setValue(plantData);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        if (str[i].equals("Cancel")){

                            dialogInterface.cancel();
                        }

                    }
                });
                builder.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView plant_name,price;
        ImageView image_view_count;
        ElegantNumberButton eBtn;
        Database db;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            plant_name = mView.findViewById(R.id.plant_name_cart);
            price = mView.findViewById(R.id.price_cart);
            eBtn = mView.findViewById(R.id.eBtn);
            image_view_count = mView.findViewById(R.id.item_count);


        }

        @Override
        public void onClick(View view) {

        }
    }
}
