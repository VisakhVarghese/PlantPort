package com.example.plantport.ClickListener;

import android.content.Context;

public interface RecyclerHomeitemclick {

    void onItemClick();
    void onLongClick(int position, String Owner_Id, Context context,String Menu_ID,String Image_url);
}
