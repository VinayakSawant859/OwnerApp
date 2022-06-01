package com.example.recyclerviewdemoproj.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.recyclerviewdemoproj.Adapters.messageAdapter;
import com.example.recyclerviewdemoproj.Models.Message;
import com.example.recyclerviewdemoproj.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    messageAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messages = new ArrayList<>();
        adapter = new messageAdapter(this, messages);
        binding.chatRecycle.setAdapter(adapter);
        binding.chatRecycle.setLayoutManager(new LinearLayoutManager(this));

        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);

        String receiverUID = getIntent().getStringExtra("child");
        String senderUId = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUId + receiverUID;
        receiverRoom = receiverUID + senderUId;

        database = FirebaseDatabase.getInstance();

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt = binding.message.getText().toString();

                Date date = new Date();
                Message message = new Message(messageTxt,senderUId,date.getTime());
                binding.message.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(message)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}