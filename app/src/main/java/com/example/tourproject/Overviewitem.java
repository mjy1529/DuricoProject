package com.example.tourproject;

import android.graphics.Bitmap;

public class Overviewitem {
    private Bitmap image;
    private String title;
    private String addr;
    private String overview;

    public Bitmap getImage() {
        return image;
    }
    public String getTitle(){
        return title;
    }
    public String getAddr(){return addr;}
    public String getOverview(){return  overview;}
    public Overviewitem(Bitmap image, String title, String addr, String overview){
        this.image = image;
        this.title = title;
        this.addr = addr;
        this.overview = overview;
    }
}