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
import android.util.Log;

import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Data;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.StoryList.StoryResult;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.MapManager;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.Util.StoryListManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//background_splash.xml, styles.xml
public class SplashActivity extends AppCompatActivity {

    NetworkService networkService;
    Handler handler;
    MapManager mapManager;
    StoryListManager storyListManager;

    public final static String TAG = "SPLASH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        networkService = Application.getInstance().getNetworkService();

        String user_id = UserManager.getInstance().getUserId();

        if (checkInternet()) { //wifi나 데이터 연결체크 : 스플래시 화면에서 map1과 map2의 데이터를 서버에서 가져오기 때문
            insertUser(user_id);
            setStoryListManager();
            setMapDataManager();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);

        } else {
            showInternetAlertDialog();
        }
    }

    public void insertUser(final String macAddress) {
        Call<String> request = networkService.insertUser(macAddress);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String user_card_url = response.body();
                    UserManager.getInstance().setUser_card_url(user_card_url);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void setMapDataManager() {
        mapManager = MapManager.getInstance();
        mapManager.initialize();

        Call<Map1Result> request = networkService.getAllMap1List();
        request.enqueue(new Callback<Map1Result>() {
            @Override
            public void onResponse(Call<Map1Result> call, Response<Map1Result> response) {
                if (response.isSuccessful()) {
                    Map1Result map1Result = response.body();
                    mapManager.setMapList(map1Result.map1);
                    for (int i = 0; i < mapManager.getMapList().size(); i++) {
                        getMap2(mapManager.getMapList().get(i).getMap_id()); //map_id로 map2 검색
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

                    mapManager.getMapList().get(Integer.parseInt(map1_id)).setMap2List(map2List);
                }
            }

            @Override
            public void onFailure(Call<Map2Result> call, Throwable t) {

            }
        });
    }

    public void setStoryListManager() {
        storyListManager = StoryListManager.getInstance();
        storyListManager.initialize();

        Call<StoryResult> request = networkService.getStoryList();
        request.enqueue(new Callback<StoryResult>() {
            @Override
            public void onResponse(Call<StoryResult> call, Response<StoryResult> response) {
                if (response.isSuccessful()) {
                    StoryResult storyResult = response.body();
                    storyListManager.setStoryList(storyResult.story);
                }
            }

            @Override
            public void onFailure(Call<StoryResult> call, Throwable t) {
                Log.d(TAG, t.getMessage());
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
