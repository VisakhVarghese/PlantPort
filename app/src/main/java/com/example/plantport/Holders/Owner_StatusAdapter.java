package com.example.plantport.Holders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.ClickListener.RecyclerItemClickListener;
import com.example.plantport.R;

public class Owner_StatusAdapter extends RecyclerView.ViewHolder  {

    public TextView Order_id,status,phone;


    public Owner_StatusAdapter(@NonNull View itemView) {
        super(itemView);

        Order_id = itemView.findViewById(R.id.order_Id);
        status = itemView.findViewById(R.id.Status);
        phone = itemView.findViewById(R.id.phone_no);


    }

}
