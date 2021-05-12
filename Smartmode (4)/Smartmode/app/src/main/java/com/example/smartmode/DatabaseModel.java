package com.example.smartmode;

public class DatabaseModel {
    private boolean create;
    private String name;
    private int btn_no;
    private boolean btn_create;
    private String btn_labl;
    private boolean ring;
    private boolean airoplane;
    private boolean bluetooth;
    private boolean wifi;
    private double location;

    public DatabaseModel(boolean create, String name, int btn_no, boolean btn_create, String btn_labl, boolean ring, boolean airoplane, boolean bluetooth, boolean wifi, double location) {
        this.create = create;
        this.name = name;
        this.btn_no = btn_no;
        this.btn_create = btn_create;
        this.btn_labl = btn_labl;
        this.ring = ring;
        this.airoplane = airoplane;
        this.bluetooth = bluetooth;
        this.wifi = wifi;
        this.location = location;
    }

    public DatabaseModel() {
    }


    @Override
    public String toString() {
        return "DatabaseModel{" +
                "create=" + create +
                ", name='" + name + '\'' +
                ", btn_no=" + btn_no +
                ", btn_create=" + btn_create +
                ", btn_labl='" + btn_labl + '\'' +
                ", ring=" + ring +
                ", airoplane=" + airoplane +
                ", bluetooth=" + bluetooth +
                ", wifi=" + wifi +
                ", location=" + location +
                '}';
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBtn_no() {
        return btn_no;
    }

    public void setBtn_no(int btn_no) {
        this.btn_no = btn_no;
    }

    public boolean isBtn_create() {
        return btn_create;
    }

    public void setBtn_create(boolean btn_create) {
        this.btn_create = btn_create;
    }

    public String getBtn_labl() {
        return btn_labl;
    }

    public void setBtn_labl(String btn_labl) {
        this.btn_labl = btn_labl;
    }

    public boolean isRing() {
        return ring;
    }

    public void setRing(boolean ring) {
        this.ring = ring;
    }

    public boolean isAiroplane() {
        return airoplane;
    }

    public void setAiroplane(boolean airoplane) {
        this.airoplane = airoplane;
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

}
