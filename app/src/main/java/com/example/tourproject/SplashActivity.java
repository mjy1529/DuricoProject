package com.example.tourproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//background_splash.xml, styles.xml
public class SplashActivity extends AppCompatActivity {

    Handler handler;

    public final static String TAG = "SPLASH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkInternet()) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showInternetAlertDialog();
                }
            }
        }, 3000);
    }

    public boolean checkInternet() { //인터넷 연결확인
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void showInternetAlertDialog() { //인터넷 연결요청 다이얼로그
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("네트워크")
                .setMessage("인터넷이 연결되어 있지 않습니다. Wifi나 LTE 연결 후 다시 실행해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alert.setCancelable(false);
        alert.show();
    }

}
