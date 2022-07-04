package com.example.plantport.Holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.ClickListener.RecyclerItemClickListener;
import com.example.plantport.Model.PlantData;

import com.example.plantport.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OwnerMenuAdapter extends RecyclerView.Adapter<OwnerMenuAdapter.ViewHolder> {

    Context mContext;
    List<PlantData>plantDataList;
    public RecyclerItemClickListener clickListener;


    public OwnerMenuAdapter(Context context, List<PlantData> list,RecyclerItemClickListener recyclerItemClickListener) {

        plantDataList = list;
        mContext = context;
        clickListener =recyclerItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.owner_menuplants,parent,false);
        return new OwnerMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PlantData plantData = plantDataList.get(position);
        Picasso.get().load(plantData.getImage_URL()).into(holder.imageView);
        holder.price.setText("Price :"+plantData.getPrice()+""+"Rs");
        holder.textView.setText(plantData.getPlant_Name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickListener.onItemClick(position,plantData.getOwnerId(),plantData.getRandomUID(),plantData.getMenu_Id());

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                clickListener.onLongItemClick(position,plantData.getMenu_Id(),plantData.getRandomUID(),plantData.getOwnerId(),plantData.getImage_URL());

              return true;
            }
        });

    }

    public void Delete(String menu_id,String Owner_id,String Plant_Id) {

        FirebaseDatabase.getInstance().getReference("PlantDetails").child(Owner_id).child(menu_id).child(Plant_Id).removeValue();
    }


    @Override
    public int getItemCount() {
        return plantDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView price;

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.price_menu);
            imageView = itemView.findViewById(R.id.owner_menuImg);
            textView = itemView.findViewById(R.id.plant_menu_owner);
        }
    }
}
