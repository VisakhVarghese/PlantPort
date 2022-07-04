package com.example.plantport.Model;

public class Users {

    String Name , Email ,Password , Phone ,CustomerId ;

    public Users() {
    }

    public Users(String customerId ,String name, String email, String password, String phone) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        CustomerId = customerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }
}
