package com.example.plantport.Model;

public class UpdatePlant {

        String Description,ImageURL,OwnerID,Plants,Price,Quantity,RandomUID;

    public UpdatePlant() {
    }

    public UpdatePlant(String description, String image_URL, String ownerId, String plant_Name, String price, String quantity, String randomUId) {
        Description = description;
        ImageURL = image_URL;
        OwnerID = ownerId;
        Plants = plant_Name;
        Price = price;
        Quantity = quantity;
        RandomUID = randomUId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getPlants() {
        return Plants;
    }

    public void setPlants(String plants) {
        Plants = plants;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
