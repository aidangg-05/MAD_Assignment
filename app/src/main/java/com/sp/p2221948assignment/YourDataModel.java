package com.sp.p2221948assignment;

// YourDataModel.java
import android.graphics.Bitmap;

public class YourDataModel {
    private String name;
    private Bitmap image;

    public YourDataModel(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }
}

