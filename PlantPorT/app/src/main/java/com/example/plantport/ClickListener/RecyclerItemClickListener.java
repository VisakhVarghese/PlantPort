package com.example.plantport.ClickListener;

public interface RecyclerItemClickListener {

    void onItemClick(int position,String Owner_id,String Plant_id,String Menu_Id);
    void onLongItemClick(int position,String Menu_id,String RandomItem,String Owner_id,String Image_Url);
}
