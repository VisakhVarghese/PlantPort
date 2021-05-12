package com.example.plantport.Holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Demo;
import com.example.plantport.Model.Owner;
import com.example.plantport.Model.PlantData;
import com.example.plantport.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class Customer_InnerAdapter extends RecyclerView.ViewHolder {

    public TextView plantName;
    public  ImageView imageView;
    public View mView;


    public Customer_InnerAdapter(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        plantName = mView.findViewById(R.id.menu_plantName);
//        price = mView.findViewById(R.id.price_menu);
        imageView = mView.findViewById(R.id.menu_img);
    }
}

//    public Context context;
//    public List<Owner> demo;
//
//    public Customer_InnerAdapter(Context mContext, List<Owner> list) {
//
//        this.context = mContext;
//        this.demo = list;
//    }
//
//
//    @NonNull
//    @Override
//    public Customer_InnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.owner_menuplants,parent,false);
//        return new Customer_InnerAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Customer_InnerAdapter.ViewHolder holder, int position) {
//
//        Owner plantData = demo.get(position);
//        Picasso.get().load(plantData.getImage_URL()).into(holder.plantImage);
//        holder.setData(position);
//        holder.plantName.setText(plantData.getCity());
//        holder.price.setText(plantData.getPlace());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return demo.size();
//    }
//
//    public class ViewHolder  extends RecyclerView.ViewHolder{
//
//        ImageView plantImage;
//        TextView price , plantName;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            plantImage = itemView.findViewById(R.id.owner_menuImg);
//            price=itemView.findViewById(R.id.price_menu);
//            plantName=itemView.findViewById(R.id.plant_menu_owner);
//        }

//        public void setData(int position) {
//
////            plantImage.setImageResource(ownerList.get(position).getImage());
//            plantName.setText(demo.get(position).getCity());
//            price.setText(demo.get(position).getPlace());
//        }
//    }
//}
