package com.example.plantport.Model;

public class Owner {

    private String City,Email,First_Name,Office_Area,OwnerId,Phone,Place,area,password;


    public Owner() {
    }

    public Owner(String city, String email, String first_Name, String office_Area, String ownerId, String phone, String place, String area, String password) {
        City = city;
        Email = email;
        First_Name = first_Name;
        Office_Area = office_Area;
        OwnerId = ownerId;
        Phone = phone;
        Place = place;
        this.area = area;
        this.password = password;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }


    public String getOffice_Area() {
        return Office_Area;
    }

    public void setOffice_Area(String office_Area) {
        Office_Area = office_Area;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
