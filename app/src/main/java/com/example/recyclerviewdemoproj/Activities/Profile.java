package com.example.recyclerviewdemoproj.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recyclerviewdemoproj.R;
import com.example.recyclerviewdemoproj.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    public static final String TAG = "1";
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    public static final String ADMINMODEL = "adminModel";
    ActivityProfileBinding profileBinding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());
        getSupportActionBar().hide();

        String email = getIntent().getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(ADMINMODEL);

        profileBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.child("dcEmail").getValue().equals("Rainbow@gmail.com")){ //When users login it will capture the email and retrieve its data
                        Glide.with(profileBinding.dcName.getContext()).load(ds.child("dcID").getValue(String.class)).into(profileBinding.dcProfile);
                        profileBinding.dcName.setText(ds.child("dcName").getValue(String.class));
                        profileBinding.dcAdminName.setText(ds.child("dcAdminName").getValue(String.class));
                        profileBinding.dcAdminEmail.setText(ds.child("dcEmail").getValue(String.class));
                        profileBinding.dcNewPass.setText(ds.child("dcNewPass").getValue(String.class));
                        profileBinding.dcConfPass.setText(ds.child("dcConfPass").getValue(String.class));
                        profileBinding.dcAddress.setText(ds.child("dcAddress").getValue(String.class));
                        profileBinding.dcPhone1.setText(ds.child("dcP1").getValue(String.class));
                        profileBinding.dcPhone2.setText(ds.child("dcP2").getValue(String.class));
                        profileBinding.dcOccupancy.setText(ds.child("dcOcc").getValue(String.class));
                        profileBinding.daycareName.setText(ds.child("dcName").getValue(String.class));
                        profileBinding.daycareEmail.setText(ds.child("dcEmail").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Unable to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
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
                        return true;
                    case R.id.nav_subscribe:
                        startActivity(new Intent(getApplicationContext(),chatDashboard.class));
                        overridePendingTransition(0,0);
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