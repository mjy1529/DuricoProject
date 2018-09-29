package com.example.tourproject;

import android.graphics.Bitmap;

public class Listviewitem {
    private Bitmap icon;
    private String name;
    private String content_id;
    private String addr;
    private String contentType_id;
    private String mapx;
    private String mapy;

    public Bitmap getIcon() {
        return icon;
    }
    public String getName(){
        return name;
    }
    public String getContent_id(){ return content_id; }
    public String getContentType_id(){ return contentType_id; }
    public String getAddr(){ return addr; }
    public String getMapx(){ return mapx; }
    public String getMapy(){ return mapy; }
    public Listviewitem(Bitmap icon,String name, String content_id, String addr, String contentType_id, String mapx, String mapy){
        this.icon = icon;
        this.name=name;
        this.content_id = content_id;
        this.addr = addr;
        this.contentType_id = contentType_id;
        this.mapx = mapx;
        this.mapy = mapy;
    }
}