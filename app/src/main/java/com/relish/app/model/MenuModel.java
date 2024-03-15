package com.relish.app.model;

public class MenuModel {
    private int icon;
    private String title;

    public MenuModel(String title2, int icon2) {
        this.title = title2;
        this.icon = icon2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon2) {
        this.icon = icon2;
    }
}
