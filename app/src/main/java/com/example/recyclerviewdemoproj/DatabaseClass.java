package com.example.recyclerviewdemoproj;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseClass extends OwnerRegister{

    private DatabaseReference databaseReference;
    public DatabaseClass(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(adminModel.class.getSimpleName());
    }
    public Task<Void> add(adminModel adminModel)
    {
        return databaseReference.push().setValue(adminModel);
    }
}
