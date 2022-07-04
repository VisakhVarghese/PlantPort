package com.example.plantport.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.plantport.Database.Database;
import com.example.plantport.Model.Owner;
import com.example.plantport.Model.Query;
import com.example.plantport.R;
import com.example.plantport.User.Display_Query;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Admin_Feedback extends Fragment {

    Toolbar toolbar;
    ListView listView;
    DatabaseReference reference;
    FirebaseListAdapter<Query> adapter;
    Query queries;
    TextView subject,comment;
    List<Query>queryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_feedbacks, null);
        Objects.requireNonNull(getActivity()).setTitle("Feedback");

        toolbar = v.findViewById(R.id.toolbar_feedback);
        toolbar.setTitle("Feedback");
        toolbar.setTitleTextAppearance(getActivity(), R.style.ToolbarTextAppearance);
        listView = v.findViewById(R.id.listview_feedback);
        queryList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Queries");

        loadData();

        return v;
    }

    private void loadData() {

        FirebaseListOptions<Query> options = new FirebaseListOptions.Builder<Query>().
                setLayout(R.layout.feedback_listview).setQuery(reference, Query.class).build();
        adapter = new FirebaseListAdapter<Query>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Query model, int position) {

                TextView username = v.findViewById(R.id.username);
                TextView phone = v.findViewById(R.id.userphone);
                TextView reason = v.findViewById(R.id.userreason);


                    username.setText("User :" + "" +model.getUser_name());
                    phone.setText("Phone :" + "" + model.getUser_Phone());
                    reason.setText("Reason :" + "" + model.getSubject());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Content");

                        LayoutInflater inflater = getLayoutInflater();
                        View view1 =inflater.inflate(R.layout.admin_query_layout,null);

                        subject = view1.findViewById(R.id.subject);
                        comment = view1.findViewById(R.id.reason);

                        subject.setText("SUBJECT  :"+" "+model.getSubject());
                        comment.setText("REASON   :"+" "+model.getReason());

                        builder.setView(view1);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });

            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
