package com.example.plantport.Holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Order;
import com.example.plantport.R;

import java.util.List;

public class OrderDetailsHolder extends RecyclerView.Adapter<OrderDetailsHolder.ViewHolder> {

    Context context;
    List<Order>orderList;

    public OrderDetailsHolder(FragmentActivity activity, List<Order> orderList) {

        this.context = activity;
        this.orderList = orderList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.order_details, parent,false);
        return new OrderDetailsHolder.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = orderList.get(position);

          holder.plantname.setText("Plant      :"+" "+order.getPlantName());
            holder.quantity.setText("Quantity :"+" "+order.getQuantity());
           holder.price.setText("Price      :"+" "+order.getPrice());
//           holder.Total.setText("Total      :"+" "+item.getAmount());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView plantname,quantity,price,Total;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            plantname = itemView.findViewById(R.id.plantname);
            quantity =itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.plantprice);
//            Total = itemView.findViewById(R.id.totalprice);

        }
    }
}
