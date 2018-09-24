package com.example.tourproject;

import android.graphics.Bitmap;

public class Listviewitem {
    private Bitmap icon;
    private String name;
    private String content_id;

    public Bitmap getIcon() {
        return icon;
    }
    public String getName(){
        return name;
    }
    public String getContent_id(){ return content_id; }
    public Listviewitem(Bitmap icon,String name, String content_id){
        this.icon = icon;
        this.name=name;
        this.content_id = content_id;
    }
}