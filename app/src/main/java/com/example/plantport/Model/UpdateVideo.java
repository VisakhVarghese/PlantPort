package com.example.plantport.Model;

public class UpdateVideo {

    String Description , Date , VideoUrl,RandomUid,Menu_Id,Plant_Id;

    public UpdateVideo() {
    }

    public UpdateVideo(String description, String date, String videoUrl, String randomUid,String Menu_Id,String Plant_Id) {
        Description = description;
        Date = date;
        VideoUrl = videoUrl;
        RandomUid = randomUid;
        this.Menu_Id =Menu_Id;
        this.Plant_Id = Plant_Id;
    }

    public String getMenu_Id() {
        return Menu_Id;
    }

    public void setMenu_Id(String menu_Id) {
        Menu_Id = menu_Id;
    }

    public String getPlant_Id() {
        return Plant_Id;
    }

    public void setPlant_Id(String plant_Id) {
        Plant_Id = plant_Id;
    }

    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getRandomUid() {
        return RandomUid;
    }

    public void setRandomUid(String randomUid) {
        RandomUid = randomUid;
    }
}
