package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.plantport.Holders.Owner_VideoAdapter;
import com.example.plantport.Model.PlantData;
import com.example.plantport.Model.UpdateVideo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.UUID;

import info.hoang8f.widget.FButton;

public class Owner_Videos extends AppCompatActivity {

    static final int CAMERA_REQUEST_CODE = 102;
    static final int VIDEO_PICK_CAMERA_REQUEST = 101;
    static final int VIDEO_PICK_GALLERY_REQUEST = 100;

    String video_date,description;
    String ownerid,plantid,menuid,videoid,videourl,dbUri,userid;
    String[] cPermission;
    MaterialEditText Description,date;
    FButton btnSelect,btnUpload;
    RecyclerView recyclerView;
    DatabaseReference ref,reference;
    StorageReference sRef;
    ActionBar actionBar;
    String randomVUid,Menu_id,Owner_Id;
    FloatingActionButton fbtn;
    private Uri videoUri;
    String Id;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Toolbar toolbar = findViewById(R.id.toolbar_video);
        toolbar.setTitle("Videos");
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        cPermission = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
        storageReference = FirebaseStorage.getInstance().getReference("Plant Videos");
         ownerid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Plant Videos");

        randomVUid = getIntent().getStringExtra("Plant_Id");
        Menu_id = getIntent().getStringExtra("Menu_Id");
        Owner_Id = getIntent().getStringExtra("Owner_Id");

        recyclerView = (RecyclerView) findViewById(R.id.video_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fbtn = (FloatingActionButton)findViewById(R.id.fab_video);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addVideos();
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Plant Videos").child(Owner_Id).child(Menu_id).child(randomVUid);
        sRef = FirebaseStorage.getInstance().getReference("Plant Videos");

    }

    private void addVideos() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Videos");
        builder.setMessage("Please add full fill Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_menu_videos,null);

        Description = (MaterialEditText)view.findViewById(R.id.videodescri);
        date = (MaterialEditText)view.findViewById(R.id.videodate);
        btnSelect = view.findViewById(R.id.btnvodeo_select);
        btnUpload=view.findViewById(R.id.btnvideo_upload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectVideo();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vdate = date.getText().toString().trim();
                String description = Description.getText().toString().trim();

                upload(vdate,description);
            }
        });

        builder.setView(view);
        builder.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void upload(String vdate, String description) {

        if (videoUri != null) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading....");
            dialog.show();

            Id = UUID.randomUUID().toString();

            sRef = storageReference.child(Id);
            ownerid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            sRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            UpdateVideo updateVideo = new UpdateVideo(description, vdate, String.valueOf(uri), Id,Menu_id,randomVUid);
                            reference.child(ownerid).child(Menu_id).child(randomVUid).child(Id).setValue(updateVideo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    dialog.dismiss();
                                    videoUri =null;
                                    Toast.makeText(getApplicationContext(), "Video Posted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception e) {
                    dialog.dismiss();

                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append("Error");
                    stringBuilder.append(e.getMessage());
                    Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded" +(int) progress+"%");
                    dialog.setCanceledOnTouchOutside(false);
//                    ProgressDialog progressDialog = dialog;
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Uploaded");
//                    stringBuilder.append((int) d);
//                    stringBuilder.append("%");
//                    progressDialog.setMessage(stringBuilder.toString());
//                    dialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0) {

            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                selectVideo();

            } else {
                Toast.makeText((Context) this, "Camera & Storage Permission are Required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
            if (requestCode == VIDEO_PICK_GALLERY_REQUEST) {
                assert data != null;
                videoUri = data.getData();
            } else if (requestCode == VIDEO_PICK_CAMERA_REQUEST) {
                assert data != null;
                videoUri = data.getData();
            }
    }


    private void selectVideo() {

        final CharSequence[] options = {"Camera", "Videos", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select From ...");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {

                    if (!checkCameraPermission()) {

                        requestCameraPermission();
                    } else {

                        videoPickCamera();

                    }
                } else if (options[item].equals("Videos")) {

                    chooseVideo();

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void videoPickCamera() {

        startActivityForResult(new Intent("android.media.action.VIDEO_CAPTURE"), VIDEO_PICK_CAMERA_REQUEST);
    }

    private void chooseVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_PICK_GALLERY_REQUEST);
    }

    private boolean checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, cPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<UpdateVideo> options = new FirebaseRecyclerOptions.Builder<UpdateVideo>()
                .setQuery(ref, UpdateVideo.class).build();
        FirebaseRecyclerAdapter<UpdateVideo, Owner_VideoAdapter> adapter = new FirebaseRecyclerAdapter<UpdateVideo, Owner_VideoAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Owner_VideoAdapter customer_videoAdapter, int i, @NonNull UpdateVideo updateVideo) {

                customer_videoAdapter.setVideo(getApplication(), updateVideo.getVideoUrl());
                customer_videoAdapter.datee.setText(updateVideo.getDate());

                customer_videoAdapter.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        plantid = updateVideo.getPlant_Id();
                        menuid = updateVideo.getMenu_Id();
                        videoid = updateVideo.getRandomUid();
                        videourl = updateVideo.getVideoUrl();

                        final CharSequence[] str = {"Update","Delete","Cancel"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Owner_Videos.this);
                        dialog.setTitle("Options");
                        dialog.setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (str[i].equals("Update")){

                                update(menuid,plantid,videoid,videourl);
                                }

                                if (str[i].equals("Delete")){

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Owner_Videos.this);
                                    builder1.setMessage("Are you sure to delete this");
                                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Delete_Video(updateVideo.getRandomUid());
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Owner_Videos.this);
                                            builder2.setMessage("Your Video has been deleted");
                                            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    dialogInterface.dismiss();

                                                }
                                            });
                                            AlertDialog dialog1 = builder2.create();
                                            dialog1.dismiss();

                                        }
                                    });
                                    builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = builder1.create();
                                    alertDialog.show();

                                }
                               else if (str[i].equals("Cancel")){

                                    dialogInterface.cancel();

                                }

                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });
            }
            @NonNull
            @Override
            public Owner_VideoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_menulist, parent, false);
                return new Owner_VideoAdapter(v,getBaseContext(),actionBar,fbtn);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void update(String menuid, String plantid, String videoid,String videourl) {

        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
        dialog1.setTitle("Update Videos");
        dialog1.setMessage("Please add full fill information");

        LayoutInflater inflater2 = getLayoutInflater();
        View view = inflater2.inflate(R.layout.add_menu_videos, null);

        Description = (MaterialEditText)view.findViewById(R.id.videodescri);
        date = (MaterialEditText)view.findViewById(R.id.videodate);
        btnSelect = view.findViewById(R.id.btnvodeo_select);
        btnUpload=view.findViewById(R.id.btnvideo_upload);

        ProgressDialog progressDialog = new ProgressDialog(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plant Videos").child(userid).child(menuid).child(plantid).child(videoid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot2) {

                UpdateVideo updateVideo = snapshot2.getValue(UpdateVideo.class);
                assert updateVideo != null;
                Description.setText(updateVideo.getDescription());
                date.setText(updateVideo.getDate());

                dbUri = videourl;
            }
        });
        dialog1.setIcon(R.drawable.ic_baseline_update_24);
        dialog1.setView(view);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectVideo();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                description = Description.getText().toString().trim();
                video_date = date.getText().toString().trim();

                if (videoUri != null){

                    newVideo();

                } else {

                    updateDb(dbUri);
                }
            }
        });

        dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        dialog1.show();

    }

    private void updateDb(String videoUri) {

        UpdateVideo updateVideo = new UpdateVideo(description, video_date, videoUri, videoid,Menu_id,randomVUid);
        FirebaseDatabase.getInstance().getReference("Plant Videos").child(ownerid).child(Menu_id).
                child(randomVUid).child(videoid).setValue(updateVideo).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {


                Toast.makeText(getApplicationContext(), "Plants Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void newVideo() {

        if (videoUri != null) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading......");
            dialog.show();


            sRef = storageReference.child(videoid);
            sRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot snapshot) {

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {

                            dialog.dismiss();
                            updateDb(String.valueOf(uri));
                            videoUri = null;
                            Toast.makeText(getApplicationContext(), "Plant Selected Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    String stringBuilder = "Failed" + e.getMessage();
                    Toast.makeText(getApplicationContext(), stringBuilder, Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double d = snapshot.getBytesTransferred() * 100.0D / snapshot.getTotalByteCount();
                    String stringBuilder = "Upload" + (int) d + "%";
                    dialog.setMessage(stringBuilder);
                    dialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    private void Delete_Video(String Video_Id) {

        FirebaseDatabase.getInstance().getReference("Plant Videos").child(Owner_Id).child(Menu_id).child(randomVUid).child(Video_Id).removeValue();
    }

}