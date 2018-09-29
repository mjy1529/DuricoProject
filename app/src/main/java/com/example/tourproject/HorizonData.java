package com.example.tourproject;

public class HorizonData {
    private int img;
    private String text;

    public HorizonData(int img, String text){
        this.img = img;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public int getImg() {
        return this.img;
    }
}
