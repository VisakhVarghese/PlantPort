package com.example.plantport.Holders;


import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Demo;
import com.example.plantport.Model.Owner;
import com.example.plantport.Model.PlantData;
import com.example.plantport.R;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
//import com.example.plantport4.Interface.ItemClickListener;

public class Customer_OuterAdapter extends  RecyclerView.ViewHolder {

    public TextView shopeName;
    public  RecyclerView rv_outer;
    public RecyclerView.LayoutManager layoutManager;
    View mView;


    public Customer_OuterAdapter(@NonNull View itemView) {
        super(itemView);

        mView =itemView;
        layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        shopeName = mView.findViewById(R.id.shope_name);
        rv_outer = mView.findViewById(R.id.inner_rv);
        rv_outer.setLayoutManager(layoutManager);


    }
}







//   public Context mContext;
//    public List<PlantData>list;
//    public List<Owner>outerList;
//
//
//    String OwnerId,Place,City;



//    public Customer_OuterAdapter( List<PlantData> list) {
//
////        this.mContext = mContext;
//        this.list = list;
//    }

//    public Customer_OuterAdapter(Context context ,List<Owner> outerList,String city,String place,String ownerId) {
//
//        this.outerList = outerList;
//        this.mContext =context;
//        this.OwnerId = city;
//        this.City = city;
//        this.Place = place;
//    }

    //        public ImageView imageView;
//
////        private ItemClickListener itemClickListener;
//        public TextView txtMenuName;
//        public TextView price;
//        View mView;
//
//
//    public Customer_OuterAdapter(@NonNull View itemView) {
//        super(itemView);
//
//        mView = itemView;
//        txtMenuName = (TextView)mView.findViewById(R.id.plant_menu_owner);
//        imageView = (ImageView)mView.findViewById(R.id.owner_menuImg);
//        price =(TextView)mView.findViewById(R.id.price_menu);
//    }
//
//    @NonNull
//    @Override
//    public Customer_OuterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view =LayoutInflater.from(mContext).inflate(R.layout.outer_rv_customerhome,parent,false);
//        return new Customer_OuterAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Customer_OuterAdapter.ViewHolder holder, int position) {
//
//        holder.setData(position);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return outerList.size();
//    }
//
//    public  class ViewHolder extends RecyclerView.ViewHolder{
//
//        RecyclerView inner_rv;
//        Customer_InnerAdapter innerAdapter;
//        List<Owner>plantDataList;
//
//        TextView shopeName;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            plantDataList = new ArrayList<>();
//
//            inner_rv = itemView.findViewById(R.id.inner_rv);
//
//            shopeName = itemView.findViewById(R.id.shope_name);
//
//            setInnerRecyclerView();
//
//        }
//
//
//
//
//        private List<Owner> getPlantDataList(){


//            demoList = new ArrayList<>();
//            demoList.add(new Demo(R.mipmap.ic_launcher,"Phonto 1"));
//            demoList.add(new Demo(R.mipmap.ic_launcher,"Phonto 1"));
//            demoList.add(new Demo(R.mipmap.ic_launcher,"Phonto 1"));
//            return demoList;

//            plantDataList = new ArrayList<>();
//            FirebaseDatabase.getInstance().getReference("Owner").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    plantDataList.clear();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                        Owner owner = dataSnapshot.getValue(Owner.class);
//                        plantDataList.add(owner);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("PlantDetails").child(OwnerId).child(City).child(Place);
//            reference1.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    plantDataList.clear();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                        PlantData plantData = dataSnapshot.getValue(PlantData.class);
//                        plantDataList.add(plantData);
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

//            return plantDataList;
//        }
//
//        private void setInnerRecyclerView() {
//
//            inner_rv.setHasFixedSize(true);
//            inner_rv.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

//            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("PlantDetails").child(OwnerId).child(City).child(Place);
//            reference1.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    plantDataList
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                        PlantData plantData = dataSnapshot.getValue(PlantData.class);
//                        plantDataList.add(plantData);
//
//                    }
//
//                    innerAdapter = new Customer_InnerAdapter(mContext,plantDataList);
//                    inner_rv.setAdapter(innerAdapter);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//                  innerAdapter = new Customer_InnerAdapter(mContext,getPlantDataList());
//                  inner_rv.setAdapter(innerAdapter);


//        }
//
//        private void setData(int position){
//
//            shopeName.setText(outerList.get(position).getFirst_Name());
//
//
//        }
//    }

//    public void onClick(View paramView) {
//            this.itemClickListener.onClick(paramView, getAdapterPosition(), false);
//        }
//
//        public void setItemClickListener(ItemClickListener paramItemClickListener) {
//            this.itemClickListener = paramItemClickListener;
//        }
//    }
