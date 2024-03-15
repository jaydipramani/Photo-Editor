package com.relish.app.model;

import java.io.Serializable;

public class PathModelPix implements Serializable {
    public Integer pathInt;
    public String pathString = "offLine";

    public Integer getPathInt() {
        return this.pathInt;
    }

    public void setPathInt(Integer pathInt2) {
        this.pathInt = pathInt2;
    }

    public String getPathString() {
        return this.pathString;
    }

    public void setPathString(String pathString2) {
        this.pathString = pathString2;
    }
}
