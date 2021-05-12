package com.example.plantport.Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.plantport.ClickListener.RecyclerHomeitemclick;
import com.example.plantport.Model.Menu;
import com.example.plantport.OwnerPlantView;
import com.example.plantport.R;
import com.google.firebase.database.FirebaseDatabase;
//import com.example.plantport4.OwnerPlants.UpdatePlant;
//import com.example.plantport4.OwnerVideoList;
import java.util.List;


public class OwnerHomeAdapter extends RecyclerView.Adapter<OwnerHomeAdapter.ViewHolder> {

    private Context context;
    private List<Menu> updatePlantList;
    public RecyclerHomeitemclick recyclerHomeitemclick;

    public OwnerHomeAdapter(Context mContext, List<Menu> list,RecyclerHomeitemclick homeItemClick) {

        updatePlantList = list;
        context = mContext;
        recyclerHomeitemclick = homeItemClick;
    }

    @NonNull
    @Override
    public OwnerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.menu_view_home, parent,false);
        return new OwnerHomeAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return updatePlantList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerHomeAdapter.ViewHolder holder, int position) {

        final Menu menu = updatePlantList.get(position);
        Glide.with(context).load(menu.getImage_URL()).into(holder.imageView);
        holder.textView.setText(menu.getMenu_Name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OwnerHomeAdapter.this.context, OwnerPlantView.class);
                intent.putExtra("RandomId", menu.getRandomId());
                OwnerHomeAdapter.this.context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                recyclerHomeitemclick.onLongClick(position,menu.getUserId(),context,menu.getRandomId(),menu.getImage_URL());
                return true;
            }
        });

    }

    public void Delete(String owner_id,String MenuId) {

        FirebaseDatabase.getInstance().getReference("MenuDetails").child(owner_id).child(MenuId).removeValue();
        FirebaseDatabase.getInstance().getReference("PlantDetails").child(owner_id).child(MenuId).removeValue();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View v) {

            super(v);
            imageView = (ImageView) v.findViewById(R.id.menu_img);
            textView = (TextView) v.findViewById(R.id.menu_plantName);
        }
    }
}
