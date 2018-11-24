package com.example.tourproject.Util;

import com.example.tourproject.Network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Application extends android.app.Application {

    private String baseDBUrl;
    private String baseImageUrl;
    private static Application instance;
    private NetworkService networkService;

    public static Application getInstance() {
        if(instance == null) {
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

        builNetworkService();
    }

    public void builNetworkService() {
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
}
