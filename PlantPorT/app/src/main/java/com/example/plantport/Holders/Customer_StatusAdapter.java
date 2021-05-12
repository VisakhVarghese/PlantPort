package com.example.plantport.Holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Requests;
import com.example.plantport.R;

import java.util.List;

public class Customer_StatusAdapter extends RecyclerView.Adapter<Customer_StatusAdapter.ViewHolder> {


    Context context;
    List<Requests>list;

    public Customer_StatusAdapter(Context context, List<Requests> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.status_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                Requests requests = list.get(position);
                holder.Order_Id.setText("#"+requests.getOrderId());
                holder.Phone.setText(requests.getMob());
                holder.Status.setText(convertCodeToStatus(requests.getStatus()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Order_Id,Status,Phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Order_Id = itemView.findViewById(R.id.order_id);
            Status = itemView.findViewById(R.id.status);
            Phone = itemView.findViewById(R.id.phone_number);
        }
    }
    private String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Accepted";
        else
            return "Ready To Deliver";
    }
}
