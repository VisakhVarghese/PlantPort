package com.example.plantport.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.Holders.Review_Adapter;
import com.example.plantport.Model.Rating;
import com.example.plantport.Model.RatingDemo;
import com.example.plantport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerReviewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference reference;
    String Id;
    List<Rating>ratingList;
    Toolbar toolbar;
    private Review_Adapter review_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pendingorders, null);
        setHasOptionsMenu(true);

        toolbar = view.findViewById(R.id.toolbar_review);
        toolbar.setTitle("Reviews");
        toolbar.setTitleTextAppearance(getActivity(),R.style.ToolbarTextAppearance);

        recyclerView = view.findViewById(R.id.recycler_review);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ratingList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Ratings");
        Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child(Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                        Rating rating = dataSnapshot1.getValue(Rating.class);
                        ratingList.add(rating);
                    }
                }
                review_adapter = new Review_Adapter(getActivity(),ratingList);
                recyclerView.setAdapter(review_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;

    }

}
