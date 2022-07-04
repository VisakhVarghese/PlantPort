package com.example.plantport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.ClickListener.RecyclerItemClickListener;
import com.example.plantport.Fragments.Owner_HomeFragmentDemo;
import com.example.plantport.Holders.OwnerMenuAdapter;
import com.example.plantport.Model.PlantData;
//import com.example.plantport4.Selection_Activity;
import com.example.plantport.User.Customer_Home;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import info.hoang8f.widget.FButton;

public class OwnerPlantView extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerItemClickListener {

        final static int PICK_IMAGE_REQUEST=1;
    private static final int RESULT_CROP = 100;

    private OwnerMenuAdapter adapter;
        String query,RandomId,randomId,ownerid;
        RecyclerView recyclerView;
        DatabaseReference reference;
        FirebaseAuth fAuth;
        MaterialEditText Price,Quantity,Description;
        MaterialEditText upPrice,upQuan,updescri;
        TextView plants;
        String dbUri,description,quantity,price,pName,Menu_Uid,Plant_id;
        FButton btn_plantUpdate,btn_plantSelect;
        FButton btnSelect,btnUpload;
        Spinner plantname;
        FirebaseStorage storage;
        StorageReference sref,storageReference;
        FloatingActionButton fBtn;
        List<PlantData> list;
        private List<PlantData> updatePlantList;
        private Uri uri = null;
        Uri imageuri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_customerhome);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle("Plants");
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.ToolbarTextAppearance);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference("Plant Images");
        fAuth = FirebaseAuth.getInstance();

             recyclerView = findViewById(R.id.recycler);
             final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            updatePlantList = new ArrayList<>();

        RandomId = getIntent().getStringExtra("RandomId");


        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("PlantDetails").child(str).child(RandomId).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {}

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updatePlantList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    PlantData plantData = (PlantData) dataSnapshot.getValue(PlantData.class);
                    updatePlantList.add(plantData);
                }

                adapter = new OwnerMenuAdapter(getApplicationContext(),updatePlantList, OwnerPlantView.this);
                recyclerView.setAdapter(adapter);
            }
        });

            fBtn = (FloatingActionButton)findViewById(R.id.fab_menu);
            fBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogBox();
                }
            });
        }

    private void dialogBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Plants");
        builder.setMessage("Please add full fill Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_menu_plants,null);

        Price = (MaterialEditText)view.findViewById(R.id.menu_price);
        Description = (MaterialEditText)view.findViewById(R.id.descri);
        Quantity = (MaterialEditText)view.findViewById(R.id.quanti);
        plantname = view.findViewById(R.id.spinner_menu);
        btnSelect = view.findViewById(R.id.btn_select_menu);
        btnUpload=view.findViewById(R.id.btn_upload_menu);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String plant_name =plantname.getSelectedItem().toString().trim();
                String price = Price.getText().toString().trim();
                String quantity = Quantity.getText().toString().trim();
                String description = Description.getText().toString().trim();

                upload(plant_name,price,quantity,description);
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

    private void upload(String pname,String pri,String qun,String descri) {


        if (uri != null) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading......");
            dialog.show();

            randomId = UUID.randomUUID().toString();
            sref = storageReference.child(randomId);
            ownerid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            sref.putFile(uri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            PlantData plantData = new PlantData(userId,randomId,pname,descri, qun , pri,String.valueOf(uri),RandomId);
                            FirebaseDatabase.getInstance().getReference("PlantDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomId).child(randomId).setValue(plantData).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    dialog.dismiss();
                                    OwnerPlant();
                                    Toast.makeText(getApplicationContext(), "Plant Posted Successfully!", Toast.LENGTH_SHORT).show();
//                                    Intent i = new Intent(getActivity(),Post_Videos.class);
//                                    i.putExtra("RandomId",Randomid);
//                                    startActivity(i);

                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded" +(int) progress+"%");
                    dialog.setCanceledOnTouchOutside(false);

                }
            });

        }
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    private void startImageCropActivity(Uri imgUri) {

        CropImage.activity(imgUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (uri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startImageCropActivity(uri);

        } else {

            Toast.makeText(getApplicationContext(), "Cancelling! Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data !=null && data.getData()!= null) {

            imageuri = data.getData();
//             Uri imgUri = CropImage.getPickImageResultUri(this,data);
//             imageuri = imgUri;
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//
//        }else{

            startImageCropActivity(imageuri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {

                assert activityResult != null;
                uri = activityResult.getUri();
//                Picasso.get().load(uri).into(imageButton);

                Toast.makeText(getApplicationContext(), "Cropped Successfully!", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getApplicationContext(), "Crop Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//            if (menuItem.getItemId() == R.id.logout) {
//                Logout();
//                return true;
//            }
////            if (menuItem.getItemId() == R.id.search_item);
//
//            return super.onOptionsItemSelected(menuItem);
//        }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        query = newText.toLowerCase();
        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("PlantDetails").child(str).child(RandomId).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {}

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                updatePlantList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    PlantData updatePlant = (PlantData) dataSnapshot.getValue(PlantData.class);
                    updatePlantList.add(updatePlant);

                    assert updatePlant != null;
                    if (updatePlant.getPlant_Name().toLowerCase().contains(query))
                        list.add(updatePlant);
                }
                adapter = new OwnerMenuAdapter(getApplicationContext(), list, OwnerPlantView.this);
                recyclerView.setAdapter(adapter);
            }
        });
        return false;
    }


        private void Logout() {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Selection_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        private void OwnerPlant() {

            String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("PlantDetails").child(str).child(RandomId).addValueEventListener(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {}

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    updatePlantList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            PlantData plantData = (PlantData) dataSnapshot.getValue(PlantData.class);
                            updatePlantList.add(plantData);
                        }

                    adapter = new OwnerMenuAdapter(getApplicationContext(),updatePlantList, OwnerPlantView.this);
                    recyclerView.setAdapter(adapter);
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.logout,menu);
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search....");
        SearchManager manager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(this.getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;

    }


    @Override
    public void onItemClick(int position,String Owner_Id,String Plant_Id,String Menu_Id) {

        Intent i = new Intent(this, Owner_Videos.class);
        i.putExtra("Owner_Id",Owner_Id);
        i.putExtra("Plant_Id",Plant_Id);
        i.putExtra("Menu_Id",Menu_Id);
        startActivity(i);
        finish();
    }

    @Override
    public void onLongItemClick(int position,String menu_id,String randomId_item ,String owner_id,String plant_url) {

        final CharSequence[] str = {"Update","Delete","Cancel"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Options");
        dialog.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (str[i].equals("Update")){

                    update(menu_id,owner_id,randomId_item,plant_url);

                }
                else if (str[i].equals("Delete")){

                    adapter.Delete(menu_id,owner_id,randomId_item );

                }else if (str[i].equals("Cancel")){

                    dialogInterface.dismiss();

                }

                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }

    private void update(String menu_id, String owner_id,String plant_id,String plant_Url) {

        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
        dialog1.setTitle("Update Plants");
        dialog1.setMessage("Please add full fill information");

        LayoutInflater inflater2 = getLayoutInflater();
        View view = inflater2.inflate(R.layout.addplant_update, null);

        updescri = view.findViewById(R.id.descri_plant);
        upPrice = view.findViewById(R.id.quanti);
        upQuan = view.findViewById(R.id.price_plant);
        plants = view.findViewById(R.id.plants);
        btn_plantUpdate = view.findViewById(R.id.btnupload_plant);
        btn_plantSelect = view.findViewById(R.id.btnselect_plant);


//        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProgressDialog progressDialog = new ProgressDialog(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PlantDetails").child(owner_id).child(menu_id).child(plant_id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot2) {

                PlantData plantData = snapshot2.getValue(PlantData.class);
                assert plantData != null;
                plants.setText(plantData.getPlant_Name());
                updescri.setText(plantData.getDescription());
                upPrice.setText(plantData.getPrice());
                upQuan.setText(plantData.getQuantity());

                dbUri = plant_Url;
                Menu_Uid = menu_id;
                Plant_id = plant_id;


            }
        });
        dialog1.setIcon(R.drawable.ic_baseline_update_24);
        dialog1.setView(view);

        btn_plantSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });
        btn_plantUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                description = updescri.getText().toString().trim();
                quantity = upQuan.getText().toString().trim();
                price =upPrice.getText().toString().trim();
                pName = plants.getText().toString().trim();

                if (imageuri != null){

                    ImageNew();


                } else {

                    updatedes(dbUri);
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

    private void updatedes(String dbUri) {

        String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        PlantData plantData = new PlantData(ownerId,Plant_id,pName,description, quantity , price,String.valueOf(dbUri),Menu_Uid);
        FirebaseDatabase.getInstance().getReference("PlantDetails").child(ownerId).child(Menu_Uid).child(Plant_id).setValue(plantData).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {

//                progressDialog.dismiss();
                Toast.makeText(OwnerPlantView.this, "Plants Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void ImageNew() {


        if (uri != null) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading......");
            dialog.show();


            sref = storageReference.child(Menu_Uid);
            sref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot snapshot) {

                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {

                            dialog.dismiss();
                            updatedes(String.valueOf(uri));
                            imageuri = null;
                            Toast.makeText(OwnerPlantView.this, "Plant Selected Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    String stringBuilder = "Failed" + e.getMessage();
                    Toast.makeText(OwnerPlantView.this, stringBuilder, Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent = new Intent(getApplicationContext(), Owner_HomeFragmentDemo.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }
}


