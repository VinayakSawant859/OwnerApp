package com.example.recyclerviewdemoproj.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recyclerviewdemoproj.Adapters.chatAdapter;
import com.example.recyclerviewdemoproj.R;
import com.example.recyclerviewdemoproj.databinding.ActivityChatDashboardBinding;
import com.example.recyclerviewdemoproj.Models.model;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatDashboard extends AppCompatActivity {

    ActivityChatDashboardBinding binding;
    FirebaseDatabase firebaseDatabase;
    ArrayList<model> models;
    com.example.recyclerviewdemoproj.Adapters.chatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        models = new ArrayList<>();

        chatAdapter = new chatAdapter(this,models);
        binding.chatRecycle.setAdapter(chatAdapter);

        firebaseDatabase.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    model model = snapshot1.getValue(com.example.recyclerviewdemoproj.Models.model.class);
                    models.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
}