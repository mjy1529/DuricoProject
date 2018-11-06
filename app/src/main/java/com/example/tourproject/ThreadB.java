package com.example.tourproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.instacart.library.truetime.TrueTime;

public class ThreadB extends Thread{
    // 해당 쓰레드가 실행되면 자기 자신의 모니터링 락을 획득
    // 5번 반복하면서 0.5초씩 쉬면서 total에 값을 누적
    // 그후에 notify()메소드를 호출하여 wiat하고 있는 쓰레드를 깨움
    @Override
    public void run(){
        synchronized(this){
            try {
                TrueTime.build().initialize();
                Log.e("TrueTime", "Connected");

            } catch (Exception e) {
                Log.i("TRUETIME ERROR", "ERROR");
                e.printStackTrace();
            }
            notify();
        }
    }
}