package com.example.smartmode.Model;

public class Button_Status {

    String BTN_ID,BTN_CREATE,BTN_NAME;

    public Button_Status() {
    }

    public Button_Status(String BTN_ID, String BTN_CREATE, String BTN_NAME) {
        this.BTN_ID = BTN_ID;
        this.BTN_CREATE = BTN_CREATE;
        this.BTN_NAME = BTN_NAME;
    }

    public String getBTN_ID() {
        return BTN_ID;
    }

    public void setBTN_ID(String BTN_ID) {
        this.BTN_ID = BTN_ID;
    }

    public String getBTN_CREATE() {
        return BTN_CREATE;
    }

    public void setBTN_CREATE(String BTN_CREATE) {
        this.BTN_CREATE = BTN_CREATE;
    }

    public String getBTN_NAME() {
        return BTN_NAME;
    }

    public void setBTN_NAME(String BTN_NAME) {
        this.BTN_NAME = BTN_NAME;
    }
}
