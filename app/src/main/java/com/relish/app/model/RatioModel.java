package com.relish.app.model;

import com.steelkiwi.cropiwa.AspectRatio;

public class RatioModel extends AspectRatio {
    private String name;
    private int selectedIem;
    private int unselectItem;

    public RatioModel(int i, int i2, int i3, int i4, String name2) {
        super(i, i2);
        this.selectedIem = i4;
        this.unselectItem = i3;
        this.name = name2;
    }

    public int getSelectedIem() {
        return this.selectedIem;
    }

    public int getUnselectItem() {
        return this.unselectItem;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
