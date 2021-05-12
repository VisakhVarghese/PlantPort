package com.example.plantport.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.plantport.Fragments.Admin_Feedback;
import com.example.plantport.Fragments.Admin_ListOwners;
import com.example.plantport.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,new Admin_ListOwners()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.admin_shops:
                fragment = new Admin_ListOwners();
                break;

            case R.id.admin_feedback:
                fragment = new Admin_Feedback();
                break;

        }
        return load(fragment);
    }

    private boolean load(Fragment fragment) {

        if (fragment != null){

            getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,fragment).commit();
            return true;
        }
        return true;
    }
}