package com.example.recyclerviewdemoproj.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recyclerviewdemoproj.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Subscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_subscribe);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeRecycle.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_subscribe:
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}