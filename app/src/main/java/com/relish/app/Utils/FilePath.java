package com.relish.app.Utils;

import android.graphics.PointF;

import java.util.ArrayList;

public class FilePath {
    int color;
    ArrayList<PointF> points;
    float r;

    public FilePath(ArrayList<PointF> arrayList, int i, float f) {
        this.points = new ArrayList<>(arrayList);
        this.color = i;
        this.r = f;
    }
}