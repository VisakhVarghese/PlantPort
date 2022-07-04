package com.example.plantport.Model;

import androidx.recyclerview.widget.LinearSmoothScroller;

import java.util.List;

public class Requests {

    private String plant_name,UserId,OrderId;
    String mob;
    String Amount,Status;
    List<Order> orderList;

    public Requests() {
    }

    public Requests(List<Order> orderList, String plant_name, String Phone, String amount,String userId,String orderId) {

        this.orderList = orderList;
        this.plant_name = plant_name;
        mob = Phone;
        Amount = amount;
        UserId = userId;
        OrderId = orderId;
        this.Status = "0"; // Default is 0 is : placed, 1 : Accepted, 2 : Ready to Deliver
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
