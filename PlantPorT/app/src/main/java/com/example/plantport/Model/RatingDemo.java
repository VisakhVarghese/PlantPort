package com.example.plantport.Model;

public class RatingDemo {

    String Phone,Plant_Id,Comment,PlantName,UserName;
    Float Rate_Value;

    public RatingDemo() {
    }

    public RatingDemo(String phone, String plant_Id, Float rate_Value, String comment, String plantName, String userName) {
        Phone = phone;
        Plant_Id = plant_Id;
        Rate_Value = rate_Value;
        Comment = comment;
        PlantName = plantName;
        UserName = userName;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPlant_Id() {
        return Plant_Id;
    }

    public void setPlant_Id(String plant_Id) {
        Plant_Id = plant_Id;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Float getRate_Value() {
        return Rate_Value;
    }

    public void setRate_Value(Float rate_Value) {
        Rate_Value = rate_Value;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}


