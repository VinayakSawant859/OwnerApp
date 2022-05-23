package com.example.recyclerviewdemoproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.recyclerviewdemoproj.databinding.ActivityDataBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Data extends AppCompatActivity {

    private ActivityDataBinding activityDataBinding;
    Animation cardsanim;
    Uri videoURI;
    MediaController mediaController;
    VideoView videoView;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDataBinding = ActivityDataBinding.inflate(getLayoutInflater());
        setContentView(activityDataBinding.getRoot());
        getSupportActionBar().setTitle("Child Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardsanim = AnimationUtils.loadAnimation(this, R.anim.cardsanim);

        activityDataBinding.cards.animate().translationY(50).setDuration(500).alpha(1.0f);
        activityDataBinding.cards.setAnimation(cardsanim);

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        activityDataBinding.videoView.setMediaController(mediaController);
        activityDataBinding.videoView.start();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("myVideos");

        Bundle bundle = getIntent().getExtras();
        activityDataBinding.cname.setText(bundle.getString("cName"));
        activityDataBinding.pname.setText("Parent: "+bundle.getString("pname"));
        CircleImageView cm = findViewById(R.id.pURL);
        Glide.with(getApplicationContext()).load(bundle.getString("cURL")).into(activityDataBinding.pURL);
        activityDataBinding.phone1.setText(bundle.getString("phone1"));
        activityDataBinding.phone2.setText(bundle.getString("phone2"));

        activityDataBinding.call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:"+ bundle.getString("phone1"));
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try {
                    startActivity(i);
                }
                catch (SecurityException s){
                    Toast.makeText(Data.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityDataBinding.call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:"+ bundle.getString("phone2"));
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try {
                    startActivity(i);
                }
                catch (SecurityException s){
                    Toast.makeText(Data.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityDataBinding.browseICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_PICK);
                                activityDataBinding.cards.setVisibility(View.INVISIBLE);
                                activityDataBinding.videoCard.animate().translationY(-100).setDuration(500).alpha(1.0f);
                                activityDataBinding.videoCard.setAnimation(cardsanim);
                                activityDataBinding.videoCard.setVisibility(View.VISIBLE);
                                activityDataBinding.titleTXT.setText(bundle.getString("cName"));
                                activityDataBinding.dateTXT.setText(date);

                                startActivityForResult(Intent.createChooser(intent,"Select Video!"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        activityDataBinding.uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processVideoUploading();
            }
        });

        activityDataBinding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDataBinding.videoCard.setVisibility(View.INVISIBLE);
                activityDataBinding.cards.setVisibility(View.VISIBLE);
            }
        });
    }

    public String getExtension(){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videoURI));
    }

    private void processVideoUploading() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Media Uplaoder");
        pd.show();
        final StorageReference uploder = storageReference.child("myVideos/"+System.currentTimeMillis()+","+getExtension());
        uploder.putFile(videoURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                fileModel obj = new fileModel(activityDataBinding.titleTXT.getText().toString(),activityDataBinding.dateTXT.getText().toString(), videoURI.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Toast.makeText(Data.this, "Succesfully Uploaded!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), HomeRecycle.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(Data.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        pd.setMessage("Uplaoded: "+(int)percent+"%");
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            videoURI=data.getData();
            videoView.setVideoURI(videoURI);

        }

    }
}