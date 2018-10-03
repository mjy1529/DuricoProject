package com.example.tourproject;

import android.os.Handler;

import java.util.ArrayList;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    ArrayList<Listviewitem> data = new ArrayList<>();
    ArrayList<Listviewitem> data2 = new ArrayList<>();
    ListviewAdapter adapter;
    ListviewAdapter adapter2;
    double mapx;
    double mapy;
    getMyGps getmygps = new getMyGps();
    MainActivity mainActivity = new MainActivity();

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {}
        }
    }
    class getMyGps extends PlaceMainActivity {

    }
}