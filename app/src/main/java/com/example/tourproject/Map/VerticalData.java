package com.example.tourproject.Map;

import com.example.tourproject.R;

public class VerticalData {
    private int img;
    private String text;
    private int state;
    private int id;
    private int img2 = R.drawable.c_1;

    public VerticalData(int img, String text, int state, int id){
        this.img = img;
        this.text = text;
        this.state = state;
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public int getImg() {
        return this.img;
    }

    public int getState(){return this.state;}

    public int getId(){return this.id;}

    public int getImg2(){return this.img2;}
}
