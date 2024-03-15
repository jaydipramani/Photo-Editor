package com.relish.app.model;

public class General {
    boolean connected;
    String typeAds;

    public General() {
    }

    public String getTypeAds() {
        return this.typeAds;
    }

    public General(String typeAds2, boolean connected2) {
        this.typeAds = typeAds2;
        this.connected = connected2;
    }

    public void setTypeAds(String typeAds2) {
        this.typeAds = typeAds2;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected2) {
        this.connected = connected2;
    }
}
