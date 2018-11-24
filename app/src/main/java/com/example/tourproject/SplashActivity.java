package com.example.tourproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tourproject.Map.Map1Data;
import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Data;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.MapDataManager;
import com.example.tourproject.Network.NetworkService;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//background_splash.xml, styles.xml
public class SplashActivity extends AppCompatActivity {

    NetworkService networkService;
    Handler handler;
    MapDataManager mapDataManager;

    public final static String TAG = "SPLASH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        networkService = Application.getInstance().getNetworkService();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkInternet()) { //wifi나 데이터가 연결체크 : 스플래시 화면에서 map1과 map2의 데이터를 서버에서 가져오기 때문
                    insertUserId(getMACAddress("wlan0"));
                    setMapList();

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showInternetAlertDialog();
                }
            }
        }, 2500);
    }

    public void insertUserId(final String macAddress) {
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

    public void setMapList() {
        mapDataManager = MapDataManager.getInstance();
        mapDataManager.initialize();

        Call<Map1Result> request = networkService.getAllMap1List();
        request.enqueue(new Callback<Map1Result>() {
            @Override
            public void onResponse(Call<Map1Result> call, Response<Map1Result> response) {
                if (response.isSuccessful()) {
                    Map1Result map1Result = response.body();
                    ArrayList<Map1Data> map1List = map1Result.map1;
                    for (int i = 0; i < map1List.size(); i++) {
                        mapDataManager.getMapList().add(map1List.get(i));
                    }
                    for (int i = 0; i < mapDataManager.getMapList().size(); i++) {
                        getMap2(mapDataManager.getMapList().get(i).getMap_id()); //map_id로 map2 검색
                    }
                }
            }

            @Override
            public void onFailure(Call<Map1Result> call, Throwable t) {
                Log.d(TAG, "map1 받아오기 실패");
            }
        });
    }

    public void getMap2(final String map1_id) {
        Call<Map2Result> request = networkService.getMap2List(map1_id);
        request.enqueue(new Callback<Map2Result>() {
            @Override
            public void onResponse(Call<Map2Result> call, Response<Map2Result> response) {
                if (response.isSuccessful()) {
                    Map2Result map2Result = response.body();
                    ArrayList<Map2Data> map2List = map2Result.map2;

                    mapDataManager.getMapList().get(Integer.parseInt(map1_id)).setMap2List(map2List);
                }
            }

            @Override
            public void onFailure(Call<Map2Result> call, Throwable t) {

            }
        });
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
//        final PrettyDialog internetDialog = new PrettyDialog(SplashActivity.this);
//        internetDialog
//                .setMessage("인터넷이 연결되어 있지 않습니다. Wifi나 LTE 연결 후 다시 실행해주세요.")
//                .setIcon(R.drawable.pdlg_icon_info)
//                .setIconTint(R.color.pdlg_color_blue)
//                .addButton("확인",
//                        R.color.pdlg_color_white,
//                        R.color.pdlg_color_blue,
//                        new PrettyDialogCallback() {
//                            @Override
//                            public void onClick() {
//                                internetDialog.dismiss();
//                                finish();
//                            }
//                        }
//                )
//                .setCanceledOnTouchOutside(false);
//
//        internetDialog.show();

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
