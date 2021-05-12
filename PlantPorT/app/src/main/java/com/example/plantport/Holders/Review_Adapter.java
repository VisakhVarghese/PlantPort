package com.example.plantport.Holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Model.Rating;
import com.example.plantport.Model.RatingDemo;
import com.example.plantport.R;

import java.util.List;

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.ViewHolder> {

    Context context;
    List<Rating>ratingList;

    public Review_Adapter(Context context, List<Rating> ratingList) {
        this.context = context;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public Review_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.review_layout,parent,false);
        return new Review_Adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Review_Adapter.ViewHolder holder, int position) {

        float value;
        holder.username.setText(ratingList.get(position).getUserName());
        holder.phoneno.setText(ratingList.get(position).getPhone());
        holder.plantName.setText(ratingList.get(position).getPlantName());
        value =Float.parseFloat(ratingList.get(position).getRate_Value());
        holder.ratingBar.setRating(value);
        holder.Comments.setText("Comment "+":"+" "+ratingList.get(position).getComment());
    }
    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username,phoneno,plantName,Comments;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user);
            phoneno = itemView.findViewById(R.id.phn);
            plantName = itemView.findViewById(R.id.plant);
            Comments = itemView.findViewById(R.id.comments);
            ratingBar = itemView.findViewById(R.id.rating_owner);
        }
    }
}
