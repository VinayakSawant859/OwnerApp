package com.example.recyclerviewdemoproj;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class myAdapter extends FirebaseRecyclerAdapter<model,myAdapter.myViewHolder>{

    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull model model) {
        holder.cName.setText(model.getChildName());
        holder.cAge.setText("Age: "+model.getChildAge());
        holder.cPhone.setText(model.getPhone1());
        Glide.with(holder.pImg.getContext()).load(model.getChildPhoto()).into(holder.pImg);


        holder.editICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.pImg.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true, 1480 )
                        .create();

                View myView = dialogPlus.getHolderView();
                final EditText url = myView.findViewById(R.id.profileURL);
                final EditText name = myView.findViewById(R.id.cName);
                final EditText age = myView.findViewById(R.id.cAge);
                final EditText parent = myView.findViewById(R.id.pName);
                final EditText address = myView.findViewById(R.id.pAddress);
                final EditText p1 = myView.findViewById(R.id.phone1);
                final EditText p2 = myView.findViewById(R.id.phone2);
                Button update = myView.findViewById(R.id.update);

                url.setText(model.getChildPhoto());
                name.setText(model.getChildName());
                age.setText(model.getChildAge());
                parent.setText(model.getParentName());
                address.setText(model.getpAddress());
                p1.setText(model.getPhone1());
                p2.setText(model.getPhone2());

                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("childPhoto",url.getText().toString());
                        map.put("childName",name.getText().toString());
                        map.put("childAge",age.getText().toString());
                        map.put("parentName",parent.getText().toString());
                        map.put("pAddress",address.getText().toString());
                        map.put("phone1",p1.getText().toString());
                        map.put("phone2",p2.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        holder.deleteICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.pImg.getContext());
                builder.setTitle("Delete Child");
                builder.setMessage("Remove child data?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.pImg.getContext(), Data.class);
                Bundle bundle = new Bundle();

                bundle.putString("cName", model.getChildName());
                bundle.putString("cURL", model.getChildPhoto());
                bundle.putString("pname", model.getParentName());
                bundle.putString("phone1", model.getPhone1());
                bundle.putString("phone2", model.getPhone2());

                i.putExtras(bundle);
                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout card;
        CircleImageView pImg;
        TextView  cName, cAge, cPhone;
        ImageView editICON, deleteICON;
        public myViewHolder(@NonNull View itemView) {
            super(itemView );
            pImg = itemView.findViewById(R.id.pURL);
            cName = itemView.findViewById(R.id.childName);
            cAge = itemView.findViewById(R.id.childAge);
            cPhone = itemView.findViewById(R.id.childPhone);
            editICON = itemView.findViewById(R.id.editICON);
            deleteICON = itemView.findViewById(R.id.deleteICON);
            card = itemView.findViewById(R.id.card);
        }
    }
}
