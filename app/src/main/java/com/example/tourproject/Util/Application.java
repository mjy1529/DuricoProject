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
    private UserManager userManager;

    public static final String TAG = "Application";

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

        initManager();
        // 사용자 정보들은 여기서 받기
        insertUser(userManager.getUserId());
        getOpenPeopleCardIdx(userManager.getUserId());
        getOpenStoryCardIdx(userManager.getUserId());
        getPlaceCard(userManager.getUserId()); //장소카드만 받아오기
        getUserMap2State(userManager.getUserId());

        getStoryPlayDataList();
    }

    public void initManager() {
        userManager = UserManager.getInstance();
        userManager.initialize();
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
                Log.d(TAG, "USER DB 데이터 INSERT 실패");
            }
        });
    }

    public void getStoryPlayDataList() {
        final StoryPlayManager storyPlayManager = StoryPlayManager.getInstance();
        storyPlayManager.initialize();

        Call<StoryPlayResult> request = networkService.getStoryPlayList();
        request.enqueue(new Callback<StoryPlayResult>() {
            @Override
            public void onResponse(Call<StoryPlayResult> call, Response<StoryPlayResult> response) {
                if(response.isSuccessful()) {
                    StoryPlayResult storyPlayResult = response.body();
                    storyPlayManager.setStoryPlayList(storyPlayResult.storyPlay);
                    Log.d(TAG, "STORY PLAY DATA 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<StoryPlayResult> call, Throwable t) {
                Log.d(TAG, "STORY PLAY DATA 받아오기 실패");
            }
        });
    }


    public void getPlaceCard(String user_id) {
        Call<PlaceResult> request = networkService.getPlaceCard(user_id);
        request.enqueue(new Callback<PlaceResult>() {
            @Override
            public void onResponse(Call<PlaceResult> call, Response<PlaceResult> response) {
                if(response.isSuccessful()) {
                    PlaceResult placeResult = response.body();
                    UserManager.getInstance().setPlaceCardList(placeResult.placeCard);
                    Log.d(TAG, "PLACE CARD 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<PlaceResult> call, Throwable t) {
                Log.d(TAG, "PLACE CARD 받아오기 실패");
            }
        });
    }

    public void getOpenPeopleCardIdx(String user_id) {
        Call<OpenPeopleCard> request = networkService.getOpenPeopleCard(user_id);
        request.enqueue(new Callback<OpenPeopleCard>() {
            @Override
            public void onResponse(Call<OpenPeopleCard> call, Response<OpenPeopleCard> response) {
                if(response.isSuccessful()) {
                    OpenPeopleCard openPeopleCard = response.body();
                    userManager.setOpenPeopleCardList(openPeopleCard.openCardList);
                    Log.d(TAG, "OPEN PEOPLE CARD LIST 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<OpenPeopleCard> call, Throwable t) {
                Log.d(TAG, "OPEN PEOPLE CARD LIST 받아오기 실패");
            }
        });
    }

    public void getOpenStoryCardIdx(String user_id) {
        Call<OpenStoryCard> request = networkService.getOpenStoryCard(user_id);
        request.enqueue(new Callback<OpenStoryCard>() {
            @Override
            public void onResponse(Call<OpenStoryCard> call, Response<OpenStoryCard> response) {
                if(response.isSuccessful()) {
                    OpenStoryCard openStoryCard = response.body();
                    userManager.setOpenStoryCardList(openStoryCard.openCardList);
                    Log.d(TAG, "OPEN STORY CARD LIST 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<OpenStoryCard> call, Throwable t) {
                Log.d(TAG, "OPEN STORY CARD LIST 받아오기 실패");
            }
        });
    }

    public void getUserMap2State(String user_id) {
        Call<UserMap2Result> request = networkService.getMap2State(user_id);
        request.enqueue(new Callback<UserMap2Result>() {
            @Override
            public void onResponse(Call<UserMap2Result> call, Response<UserMap2Result> response) {
                if(response.isSuccessful()) {
                    UserMap2Result userMap2Result = response.body();
                    userManager.setMap2StateList(userMap2Result.map2State);
                    Log.d(TAG, "MAP2 STATE 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<UserMap2Result> call, Throwable t) {
                Log.d(TAG, "MAP2 STATE 받아오기 실패");
            }
        });
    }
}
