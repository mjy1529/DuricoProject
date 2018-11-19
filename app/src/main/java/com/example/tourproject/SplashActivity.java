package com.example.tourproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tourproject.Network.Application;
import com.example.tourproject.Network.NetworkService;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

//background_splash.xml, styles.xml
public class SplashActivity extends AppCompatActivity {

    NetworkService networkService;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        insertUserId(getMACAddress("wlan0"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    public void insertUserId(final String macAddress) {
        networkService = Application.getInstance().getNetworkService();

        new AsyncTask<Void, Void, String>() {
            String result;
            @Override
            protected String doInBackground(Void... voids) {
                Call<String> request = networkService.insertUserId(macAddress);
                try {
                    result = request.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        }.execute();
    }

    //맥 주소 받아오기
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                if (interfaceName != null) {
                    if (!networkInterface.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++) {
                    buf.append(String.format("%02X:", mac[idx]));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
