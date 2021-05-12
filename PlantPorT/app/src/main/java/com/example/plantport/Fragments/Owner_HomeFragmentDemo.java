package com.example.plantport.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.plantport.ClickListener.RecyclerHomeitemclick;
import com.example.plantport.Common.Common;
import com.example.plantport.Holders.OwnerHomeAdapter;
import com.example.plantport.Model.Menu;
import com.example.plantport.Model.Owner;
import com.example.plantport.R;
import com.example.plantport.Selection_Activity;
import com.example.plantport.Service.ListenerOrderStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.app.Activity.RESULT_OK;

public class Owner_HomeFragmentDemo extends Fragment implements SearchView.OnQueryTextListener, RecyclerHomeitemclick {

    final static int PICK_IMAGE_REQUEST = 1;

    private OwnerHomeAdapter adapter;
    String Randomid, ownerid, Menu_Uid;
    RecyclerView recyclerView;
    String query, dbUri, Menu_name;
    StorageReference sref, storageReference;
    DatabaseReference reference, ref;
    FirebaseAuth fAuth;
    SwipeRefreshLayout refreshLayout;
    FirebaseStorage storage;
    MaterialEditText materialEditText, materialEditText_update;
    FButton btnUpload, btnSelect, btnUpload_update, btnSelect_update;
    com.google.android.material.floatingactionbutton.FloatingActionButton fCart;
    private List<Menu> updatePlantList;
    List<Menu> list;
    private Uri uri = null;
    Uri imageuri=null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_customer__home_fragment_demo, null);
        Objects.requireNonNull(getActivity()).setTitle("Home");
        setHasOptionsMenu(true);

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar_menu);
        toolbar.setTitle("Menu");
        toolbar.setTitleTextAppearance(getActivity(),R.style.ToolbarTextAppearance);
        ((AppCompatActivity)getActivity()).getDelegate().setSupportActionBar(toolbar);

        assert container != null;
        recyclerView = view.findViewById(R.id.menu_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        updatePlantList = new ArrayList<>();

        refreshLayout = view.findViewById(R.id.swipelayout);
        refreshLayout.setColorSchemeResources(R.color.official_color,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Common.isConnectedToInternet(Objects.requireNonNull(getActivity()))) {

                    OwnerPlant();

                }else {

                    Toast.makeText(getActivity(), "Please connect internet!!", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (Common.isConnectedToInternet(Objects.requireNonNull(getActivity()))) {

                    OwnerPlant();

                }else {

                    Toast.makeText(getActivity(), "Please connect internet!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Plant Images");
        fAuth = FirebaseAuth.getInstance();

        fCart = (FloatingActionButton) view.findViewById(R.id.fab_home);
        fCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();
            }

            private void showDialog() {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                dialog.setTitle("Add New Category");
                dialog.setMessage("Please add full fill information");

                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.addmenu, null);

                materialEditText = view1.findViewById(R.id.name_menu);
                btnSelect = view1.findViewById(R.id.btn_select);
                btnUpload = view1.findViewById(R.id.btn_upload);

                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectImage();

                    }
                });
                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String plant_name = materialEditText.getText().toString().trim();

                        upload(plant_name);
                    }
                });

                dialog.setView(view1);
                dialog.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Owner").child(str);
        reference = databaseReference;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Owner owner = snapshot.getValue(Owner.class);
                assert owner != null;
                if (Common.isConnectedToInternet(getActivity())) {

                    OwnerPlant();

                }else {

                    Toast.makeText(getActivity(), "Please connect internet!!", Toast.LENGTH_SHORT).show();
                }

                Intent service = new Intent(getActivity(), ListenerOrderStatus.class);
                service.putExtra("Id",str);
                Objects.requireNonNull(Objects.requireNonNull(getActivity())).startService(service);

            }
        });
        return view;
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull android.view.Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    private void upload(String Plant_name) {

        if (uri != null) {

            final ProgressDialog dialog = new ProgressDialog((getActivity()));
            dialog.setTitle("Uploading......");
            dialog.show();

            Randomid = UUID.randomUUID().toString();
            sref = storageReference.child(Randomid);
            ownerid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            sref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            Menu menu = new Menu(Plant_name, String.valueOf(uri), userId, Randomid);
                            FirebaseDatabase.getInstance().getReference("MenuDetails").child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).child(Randomid).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Plant Posted Successfully!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded" + (int) progress + "%");
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

        CropImage.activity(imgUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(getActivity(),this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (uri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startImageCropActivity(uri);

        } else {

            Toast.makeText(getActivity(), "Cancelling! Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageuri = data.getData();

            startImageCropActivity(imageuri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                assert activityResult != null;
                uri = activityResult.getUri();
                Toast.makeText(getActivity(), "Cropped Successfully!", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getActivity(), "Crop Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void OwnerPlant() {

        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("MenuDetails").child(str).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updatePlantList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu1 = (Menu) dataSnapshot.getValue(Menu.class);
                    updatePlantList.add(menu1);
                }
                adapter = new OwnerHomeAdapter(getActivity(), updatePlantList, Owner_HomeFragmentDemo.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.log) {
            Logout();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void Logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), Selection_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {

        query = text.toLowerCase();
        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("MenuDetails").child(str).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                updatePlantList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Menu menu = (Menu) dataSnapshot.getValue(Menu.class);
                    updatePlantList.add(menu);

                    assert menu != null;
                    if (menu.getMenu_Name().toLowerCase().contains(query))
                        list.add(menu);
                }
                adapter = new OwnerHomeAdapter(getContext(), list, Owner_HomeFragmentDemo.this);
                recyclerView.setAdapter(adapter);
            }
        });
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull android.view.Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.common, menu);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
        SearchManager manager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

    }
    @Override
    public void onItemClick() {

    }

    @Override
    public void onLongClick(int position, String owner_id, Context context, String Menu_id,String Image_url) {

        final CharSequence[] str = {"Update", "Delete", "Cancel"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Options");
        dialog.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (str[i].equals("Update")) {

                    update(Menu_id,Image_url);

                } else if (str[i].equals("Delete")) {

                    adapter.Delete(owner_id,Menu_id);

                } else if (str[i].equals("Cancel")) {

                    dialogInterface.dismiss();

                }

                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    public void update(String menu_id,String menu_url) {

        AlertDialog.Builder dialog1 = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        dialog1.setTitle("Update Category");
        dialog1.setMessage("Please add full fill information");

        LayoutInflater inflater2 = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater2.inflate(R.layout.addmenu_update, null);

        materialEditText_update = view.findViewById(R.id.name_menupdate);
        btnSelect_update = view.findViewById(R.id.btn_select_update);
        btnUpload_update = view.findViewById(R.id.btn_update);


        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        ref = FirebaseDatabase.getInstance().getReference("MenuDetails").child(str).child(menu_id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                Menu menu1 = snapshot1.getValue(Menu.class);
                assert menu1 != null;
                materialEditText_update.setText(menu1.getMenu_Name());

                dbUri = menu_url;
                Menu_Uid = menu_id;

            }
        });
        dialog1.setIcon(R.drawable.ic_baseline_update_24);
        dialog1.setView(view);

        btnSelect_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectNewImage();
            }
        });
        btnUpload_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Menu_name = materialEditText_update.getText().toString().trim();

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

        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("MenuDetails");
        storage = FirebaseStorage.getInstance();
        sref = storage.getReference();

    }
    private void updatedes(String dbUri) {

        String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Menu menu = new Menu(Menu_name, String.valueOf(dbUri), ownerId, Menu_Uid);
        FirebaseDatabase.getInstance().getReference("MenuDetails").child(ownerId).child(Menu_Uid)
                .setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getActivity(), "Plants Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void selectNewImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

    }
    private void ImageNew() {

        if (uri != null) {

            final ProgressDialog dialog = new ProgressDialog((getActivity()));
            dialog.setTitle("Uploading......");
            dialog.show();

            sref = this.storageReference.child(Menu_Uid);
            sref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {

                            dialog.dismiss();
                            updatedes(String.valueOf(uri));
                            imageuri = null;
                            Toast.makeText(getActivity(), "Plant Selected Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    String stringBuilder = "Failed" + e.getMessage();
                    Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_SHORT).show();
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
}