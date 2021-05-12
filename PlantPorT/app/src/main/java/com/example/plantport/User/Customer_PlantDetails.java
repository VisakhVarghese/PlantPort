package com.example.plantport.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.plantport.Database.Database;
import com.example.plantport.Model.Order;
import com.example.plantport.Model.PlantData;
import com.example.plantport.Model.Rating;
import com.example.plantport.Model.Requests;
import com.example.plantport.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Customer_PlantDetails extends AppCompatActivity implements RatingDialogListener {

    FloatingActionButton btnCart,btnRate;
    RatingBar ratingBar;
    Integer stock;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton numberButton;
    TextView plant_Description;
    List<Order>orderList = new ArrayList<>();
    ImageView plant_image;
    TextView plant_name;
    TextView plant_price;
    DatabaseReference reference,ref,reff;
    String plantName,price,PlantId,menu_Id,Phone,UserId,OwnerId,Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__plant_details);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance();
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(2132017523);
        reference = FirebaseDatabase.getInstance().getReference("Ratings");
        UserId = FirebaseAuth.getInstance().getCurrentUser().
                getUid();

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btn_cart);
        btnRate = (FloatingActionButton) findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.rating);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRatingDialog();
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int value = 0;
                try {
                    value = Integer.parseInt(numberButton.getNumber());
                } catch (NumberFormatException e) {
                    // Log error, change value of temperature, or do nothing
                }

//                String value = String.valueOf(numberButton);
                ref = FirebaseDatabase.getInstance().getReference().child("PlantDetails").child(OwnerId).child(menu_Id).child(PlantId);
                int finalValue = value;
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        PlantData plantData = snapshot.getValue(PlantData.class);
                        assert plantData != null;
                        stock = Integer.parseInt(plantData.getQuantity());

                        checkStock(stock, finalValue);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



//                new Database(getBaseContext()).addToCart(new Order(UserId,
//                        PlantId,
//                        plantName,
//                        numberButton.getNumber(),
//                        price));
//                Toast.makeText(Customer_PlantDetails.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(getApplicationContext(), Cart.class));
//                finish();

//                (new Database(PlantDetails.this.getBaseContext())).addToCart(new Order(PlantDetails.this.plantId, PlantDetails.this.plant.getName(), PlantDetails.this.numberButton.getNumber(), PlantDetails.this.plant.getPrice(), PlantDetails.this.plant.getDiscount()));
//                Toast.makeText((Context) PlantDetails.this, "Added to Cart", 0).show();
            }
        });


        plant_Description = (TextView) findViewById(R.id.plant_description);
        plant_name = (TextView) findViewById(R.id.plant_name);
        plant_image = (ImageView) findViewById(R.id.img_plant);
        plant_price = (TextView) findViewById(R.id.plant_price);

        if (getIntent() != null) {

             plantName = getIntent().getStringExtra("PlantName");
             price = getIntent().getStringExtra("Price");
             PlantId = getIntent().getStringExtra("plantId");
            String description = getIntent().getStringExtra("Description");
            String image = getIntent().getStringExtra("PlantImage");
            String data = getIntent().getStringExtra("Data");
            menu_Id = getIntent().getStringExtra("MenuId");
            Phone = getIntent().getStringExtra("Phone");
            Username = getIntent().getStringExtra("UserName");
            OwnerId = getIntent().getStringExtra("OwnerId");

            assert data != null;
            if (data.equals("PlantData"))

                getDetailedPlant(plantName,price,description,image);

        }
        getRatingPlant(PlantId);
    }

    private void checkStock(Integer stock,int value) {

        if (stock == 0 || stock < Integer.parseInt(String.valueOf(value))){

            Toast.makeText(this, "Out of Stock!!", Toast.LENGTH_SHORT).show();

        }else{

            addToCart(stock,value);
        }
    }

    private void addToCart(Integer stock,int value) {

        lessStock(stock,value);
        orderList = new Database(getApplicationContext()).getCarts();


        if (orderList.isEmpty()){

            new Database(getBaseContext()).addToCart(new Order(OwnerId,UserId,menu_Id,
                    PlantId,
                    plantName,
                    numberButton.getNumber(),
                    price));
            Toast.makeText(Customer_PlantDetails.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

        }else {
            for (int i = 0 ; i < orderList.size() ; i++){

                if (orderList.get(i).getUserId().equals(UserId)) {

                    if (orderList.get(i).getOwnerId().equals(OwnerId)) {
                        {
//                            if (PlantId.equals(orderList.get(i).getPlantId())) {
//
//                                Toast.makeText(this, "This plant already added to cart!", Toast.LENGTH_SHORT).show();
//
//                            } else {

                                new Database(getBaseContext()).addToCart(new Order(OwnerId, UserId,menu_Id,
                                        PlantId,
                                        plantName,
                                        numberButton.getNumber(),
                                        price));
                                Toast.makeText(Customer_PlantDetails.this, "Added to Cart ", Toast.LENGTH_SHORT).show();

                            }
//                        }
                    } else {
                        Toast.makeText(Customer_PlantDetails.this, "You can only select same nursery plants at a time!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    new Database(getBaseContext()).addToCart(new Order(OwnerId,UserId,menu_Id,
                            PlantId,
                            plantName,
                            numberButton.getNumber(),
                            price));
                    Toast.makeText(Customer_PlantDetails.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void lessStock(Integer stock, int value) {

        reff = FirebaseDatabase.getInstance().getReference().child("PlantDetails").child(OwnerId).child(menu_Id).child(PlantId);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PlantData plantData = snapshot.getValue(PlantData.class);
                assert plantData != null;
                int qua = Integer.parseInt(plantData.getQuantity());

                Integer finalValue = qua-value;
                plantData.setQuantity(finalValue.toString());
                reff.setValue(plantData);

//                Toast.makeText(Customer_PlantDetails.this, "Stock less", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getRatingPlant(String plantId) {

        Query plantRating = reference.child(OwnerId).child(PlantId).orderByChild("plant_Id").equalTo(plantId);
        plantRating.addValueEventListener(new ValueEventListener() {
            int count=0 ,sum =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Rating item = dataSnapshot.getValue(Rating.class);
                    assert item != null;
                    sum+= Integer.parseInt(item.getRate_Value());
                    count++;

                }

                if (count != 0) {

                    float average = sum/count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showRatingDialog() {

        new AppRatingDialog.Builder().setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Note Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1).setTitle("Rate this plant")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.official_color)
                .setDescriptionTextColor(R.color.official_color)
                .setHint("Please write your comment here....")
                .setHintTextColor(R.color.official_color)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.Text_color)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(Customer_PlantDetails.this).show();
    }

    private void getDetailedPlant(String plant_Name, String price, String description,String Image) {

        plant_Description.setText(description);
        plant_name.setText(plant_Name);
        plant_price.setText(price);
        Picasso.get().load(Image).into(plant_image);
        collapsingToolbarLayout.setTitle(plant_Name);
    }

    @Override
    public void onNegativeButtonClicked() {



    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {

        //Getting rating and upload to firebase
        Rating rating = new Rating(Phone,PlantId,String.valueOf(value),comments,plantName,Username);
        reference.child(OwnerId).child(PlantId).child(Phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.child(Phone).exists()){

                    //Remove old value
                    reference.child(OwnerId).child(PlantId).child(Phone).removeValue();
                    addNew(Phone,rating);
                    //Update new value
//                    reference.child(Phone).setValue(rating);
                }else{

                    reference.child(OwnerId).child(PlantId).child(Phone).setValue(rating);
                }
                Toast.makeText(Customer_PlantDetails.this, "Thank you for submitting rating!!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addNew(String phone, Rating rating) {

        reference.child(OwnerId).child(PlantId).child(phone).setValue(rating);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Customer_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
