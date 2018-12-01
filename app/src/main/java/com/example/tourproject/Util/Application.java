package com.example.tourproject.Util;

import android.util.Log;

import com.example.tourproject.CardBox.CardResult;
import com.example.tourproject.CardBox.OpenPeopleCard;
import com.example.tourproject.CardBox.OpenStoryCard;
import com.example.tourproject.CardBox.PlaceResult;
import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Data;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.Map.UserMap2Result;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.StoryList.StoryResult;
import com.example.tourproject.StoryPlay.StoryPlayResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Application extends android.app.Application {

    private String baseDBUrl;
    private String baseImageUrl;
    private static Application instance;
    private NetworkService networkService;

    public static final String TAG = "Application";

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        baseDBUrl = "http://52.78.143.26:80";
        baseImageUrl = "https://s3.ap-northeast-2.amazonaws.com/smarttourapp";

        buildNetworkService();
        initManager();
        insertUser(UserManager.getInstance().getUserId());
    }

    public void buildNetworkService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(Application.getInstance().getBaseDBUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public String getBaseDBUrl() {
        return baseDBUrl;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void initManager() {
        UserManager.getInstance().initialize();
        CardManager.getInstance().initialize();
        MapManager.getInstance().initialize();
        StoryListManager.getInstance().initialize();
    }

    public void insertUser(final String macAddress) {
        Call<String> request = networkService.insertUser(macAddress);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String user_card_url = response.body();
                    UserManager.getInstance().setUser_card_url(user_card_url);
                    Log.d(TAG, "USER DB 데이터 INSERT 성공");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "USER DB 데이터 INSERT 실패 " + t.getMessage());
            }
        });
    }
}
