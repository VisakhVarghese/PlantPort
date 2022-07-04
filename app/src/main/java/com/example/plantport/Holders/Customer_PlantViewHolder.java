package com.example.plantport.Holders;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.R;

public class Customer_PlantViewHolder extends RecyclerView.ViewHolder {

    public TextView Price , PlantName;
    public ImageView imageView;
    View mView;

    public Customer_PlantViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        Price = mView.findViewById(R.id.price_menu);
        PlantName = mView.findViewById(R.id.plant_menu_owner);
        imageView = mView.findViewById(R.id.owner_menuImg);


    }
}
