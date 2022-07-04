package com.example.plantport.Model;

public class Menu {

    String Menu_Name,Image_URL,UserId,RandomId;

    public Menu() {
    }

    public Menu(String menu_Name, String image_URL, String userId, String randomId) {
        Menu_Name = menu_Name;
        Image_URL = image_URL;
        UserId = userId;
        RandomId = randomId;
    }

    public String getMenu_Name() {
        return Menu_Name;
    }

    public void setMenu_Name(String menu_Name) {
        Menu_Name = menu_Name;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRandomId() {
        return RandomId;
    }

    public void setRandomId(String randomId) {
        RandomId = randomId;
    }
}
