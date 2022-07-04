package com.example.plantport.Model;

public class Order {

    String OwnerId,UserId,MenuId,PlantId,PlantName,Quantity,Price;

    public Order() {
    }

    public Order(String ownerId, String userId, String menuId, String plantId, String plantName, String quantity, String price) {
        OwnerId = ownerId;
        UserId = userId;
        MenuId = menuId;
        PlantId = plantId;
        PlantName = plantName;
        Quantity = quantity;
        Price = price;
    }

    //    public String getID() {
//        return ID;
//    }
//
//    public void setID(String ID) {
//        this.ID = ID;
//    }


    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPlantId() {
        return PlantId;
    }

    public void setPlantId(String plantId) {
        PlantId = plantId;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
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
}
