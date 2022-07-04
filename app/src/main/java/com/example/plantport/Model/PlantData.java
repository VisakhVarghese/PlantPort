package com.example.plantport.Model;

public class PlantData {

    String OwnerId,RandomUID,Plant_Name,Description,Quantity,Price,Image_URL,Menu_Id;

    public PlantData() {
    }

    public PlantData(String ownerId, String randomUID, String plant_Name, String description, String quantity, String price, String image_URL,String menu_Id) {
        OwnerId = ownerId;
        RandomUID = randomUID;
        Plant_Name = plant_Name;
        Description = description;
        Quantity = quantity;
        Price = price;
        Image_URL = image_URL;
        Menu_Id =menu_Id;
    }

    public String getMenu_Id() {
        return Menu_Id;
    }

    public void setMenu_Id(String menu_Id) {
        Menu_Id = menu_Id;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getPlant_Name() {
        return Plant_Name;
    }

    public void setPlant_Name(String plant_Name) {
        Plant_Name = plant_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }
}
