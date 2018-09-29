package com.example.tourproject;

public class VerticalData {
    private int img;
    private String text;
    private int state;

    public VerticalData(int img, String text, int state){
        this.img = img;
        this.text = text;
        this.state = state;
    }

    public String getText() {
        return this.text;
    }

    public int getImg() {
        return this.img;
    }

    public int getState(){return this.state;}

    public void setState(){this.state = state;}
}
