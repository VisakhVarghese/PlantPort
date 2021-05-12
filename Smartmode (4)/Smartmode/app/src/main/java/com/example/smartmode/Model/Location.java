package com.example.smartmode.Model;

public class Location {

    String SETTINGS_ID,WIFI,BLU,RING,AIROPLANE,ENABLE;

    public Location(String SETTINGS_ID, String WIFI, String BLU, String RING, String AIROPLANE, String ENABLE) {
        this.SETTINGS_ID = SETTINGS_ID;
        this.WIFI = WIFI;
        this.BLU = BLU;
        this.RING = RING;
        this.AIROPLANE = AIROPLANE;
        this.ENABLE = ENABLE;
    }

    public String getSETTINGS_ID() {
        return SETTINGS_ID;
    }

    public void setSETTINGS_ID(String SETTINGS_ID) {
        this.SETTINGS_ID = SETTINGS_ID;
    }

    public String getWIFI() {
        return WIFI;
    }

    public void setWIFI(String WIFI) {
        this.WIFI = WIFI;
    }

    public String getBLU() {
        return BLU;
    }

    public void setBLU(String BLU) {
        this.BLU = BLU;
    }

    public String getRING() {
        return RING;
    }

    public void setRING(String RING) {
        this.RING = RING;
    }

    public String getAIROPLANE() {
        return AIROPLANE;
    }

    public void setAIROPLANE(String AIROPLANE) {
        this.AIROPLANE = AIROPLANE;
    }

    public String getENABLE() {
        return ENABLE;
    }

    public void setENABLE(String ENABLE) {
        this.ENABLE = ENABLE;
    }
}
