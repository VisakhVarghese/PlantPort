package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;


import com.example.plantport.Fragments.Owner_HomeFragmentDemo;
import com.example.plantport.Fragments.OwnerOrderfragment;
import com.example.plantport.Fragments.OwnerReviewFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Owner_Home extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.owner_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.owner_fragment_container,new Owner_HomeFragmentDemo()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.owner_home:
                fragment = new Owner_HomeFragmentDemo();
                break;

            case R.id.owner_pending:
                fragment = new OwnerReviewFragment();
                break;

            case R.id.owner_orders:

                fragment = new OwnerOrderfragment();
                break;

        }
        return loadownerfragment(fragment);

    }

    private boolean loadownerfragment(Fragment fragment) {

        if (fragment != null){

            getSupportFragmentManager().beginTransaction().replace(R.id.owner_fragment_container,fragment).commit();
            return true;
        }
        return false;
    }


}

