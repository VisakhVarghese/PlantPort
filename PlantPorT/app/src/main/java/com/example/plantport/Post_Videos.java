package com.example.plantport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.plantport.Model.PlantData;
import com.example.plantport.Model.UpdateVideo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Post_Videos extends AppCompatActivity {

    static final int CAMERA_REQUEST_CODE = 102;
    static final int VIDEO_PICK_CAMERA_REQUEST = 101;
    static final int VIDEO_PICK_GALLERY_REQUEST = 100;

    public static final int MEDIA_TYPE_VIDEO = 1;

    String Date,str;
    String[] cPermission;
    Button choose;
    String city;
    EditText date;
    String desc;
    EditText description;
    MediaController mController;
    String ownerid,Menu_id,Owner_Id;
    String place;
    private List<PlantData> plantDataList;
    String plantname;
    Button play;
    Button post;
    String randomUid;
    String plant_Id;
    DatabaseReference ref;
    DatabaseReference reference;
    StorageReference sRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri videoUri;
    VideoView videoView;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__videos);

        Toolbar toolbar = findViewById(R.id.toolbar_postvideo);
        toolbar.setTitle("Post Videos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        choose = (Button) findViewById(R.id.btn_choosevideo);
        play = (Button) findViewById(R.id.btn_playvideo);
        post = (Button) findViewById(R.id.btn_video);
        description = (EditText) findViewById(R.id.video_desc);
        date = (EditText) findViewById(R.id.video_date);

        plantDataList = new ArrayList<>();

        plant_Id = getIntent().getStringExtra("PlantId");
        Menu_id = getIntent().getStringExtra("MenuId");
        Owner_Id = getIntent().getStringExtra("OwnerId");

        videoView = (VideoView) findViewById(R.id.owner_videos);

        cPermission = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

        storageReference = FirebaseStorage.getInstance().getReference("Plant Videos");
         str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Plant Videos");

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Owner owner = snapshot.getValue(Owner.class);
//                assert owner != null;
//                city = owner.getCity();
//                place = owner.getArea();
//            }
//        });
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                desc = description.getText().toString();
                Date = date.getText().toString().trim();
                uploadVideo();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                videoPickDialg();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0) {

            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                videoPickCamera();

            } else {
                Toast.makeText((Context) this, "Camera & Storage Permission are Required", Toast.LENGTH_SHORT).show();
            }
        }
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void videoPickDialg() {


        final CharSequence[] options = {"Camera", "Videos", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Post_Videos.this);
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

                    // Pick Image from Gallery
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Videos")) {

                    chooseVideo();
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

//        final String[] item = {"Camera", "Gallery", " Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext()).setTitle("Pick Video From");
//        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                if (i == 0) {
//
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission();
//                    } else {
//                        videoPickCamera();
//                    }
//                } else if (i == 1) {
//                    chooseVideo();
//                } else if (i == 2) {
//
//                    dialog.dismiss();
//
//                }
//            }
//        };
//        builder.setItems( new String[]{"Camera", "Gallery","Cancel"}, onClickListener).show();
//    }

//        builder.setItems(item, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                if (item[i].equals("Camera ")) {
//
//                    if (!checkCameraPermission()) {
//
//                        requestCameraPermission();
//
//                    } else {
//                        videoPickCamera();
//                    }
//
//                } else if (item[i].equals(("Gallery"))) {
//
//                    chooseVideo();
//
//                } else if (item[i].equals("Cancel")) {
//
//                    dialog.dismiss();
//                }
//
//            }
//        });
//    }


//        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (i == String[0])
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission();
//                    } else {
//                        videoPickCamera();
//                    }
//               else if (i == 1) {
//                    chooseVideo();
//                }else if (i == 2) {
//
//                   dialogInterface.dismiss();
//
//                }
//            }
//        };
//        builder.setItems((CharSequence[])new String[] { "Camera", "Gallery" }, onClickListener).show();
//    }

    public void chooseVideo() {
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

    private void setVideoTovideoView() {
        MediaController mediaController = new MediaController((Context) this);
        mController = mediaController;
        mediaController.setAnchorView((View) videoView);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(mController);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.pause();
            }
        });
    }

    private void uploadVideo() {

        if (videoUri != null) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading....");
            dialog.show();
            String uid = UUID.randomUUID().toString();

            sRef = storageReference.child(uid);
            ownerid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            sRef.putFile(this.videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            UpdateVideo updateVideo = new UpdateVideo(desc, Date, String.valueOf(uri), uid,Menu_id,plant_Id);
                            reference.child(ownerid).child(Menu_id).child(plant_Id).child(uid).setValue(updateVideo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    dialog.dismiss();
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


    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt2 == -1)
            if (paramInt1 == 100) {
                this.videoUri = paramIntent.getData();
//                setVideoTovideoView();
            } else if (paramInt1 == 101) {
                this.videoUri = paramIntent.getData();
//                setVideoTovideoView();
            }
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void videoPickCamera() {

      startActivityForResult(new Intent("android.media.action.VIDEO_CAPTURE"), VIDEO_PICK_CAMERA_REQUEST);
    }


}
