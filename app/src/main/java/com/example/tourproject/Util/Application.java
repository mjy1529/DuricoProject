package com.example.tourproject.Util;

import android.util.Log;

import com.example.tourproject.CardBox.CardResult;
import com.example.tourproject.CardBox.OpenPeopleCard;
import com.example.tourproject.CardBox.OpenStoryCard;
import com.example.tourproject.CardBox.PlaceResult;
import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Data;
import com.example.tourproject.Map.Map2Result;
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
    private CardManager cardManager;
    private MapManager mapManager;
    private StoryListManager storyListManager;

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

        //상세이야기, 전체 카드 받아오기
        setStoryListManager();
        getCardList();
        setMapDataManager();
        getStoryPlayDataList();

        insertUser(userManager.getUserId());
        getOpenPeopleCardIdx(userManager.getUserId());
        getOpenStoryCardIdx(userManager.getUserId());

        getPlaceCard(userManager.getUserId()); //장소카드만 받아오기
    }

    public void initManager() {
        cardManager = CardManager.getInstance();
        cardManager.initialize();

        mapManager = MapManager.getInstance();
        mapManager.initialize();

        storyListManager = StoryListManager.getInstance();
        storyListManager.initialize();

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
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void setMapDataManager() {
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
                }
            }

            @Override
            public void onFailure(Call<StoryPlayResult> call, Throwable t) {

            }
        });
    }

    public void getCardList() {
        Call<CardResult> request = networkService.getCard();
        request.enqueue(new Callback<CardResult>() {
            @Override
            public void onResponse(Call<CardResult> call, Response<CardResult> response) {
                if(response.isSuccessful()) {
                    CardResult cardResult = response.body();
                    for(int i=0; i<cardResult.card.size(); i++) {
                        switch (cardResult.card.get(i).getCard_category()) {
                            case "people" :
                                cardManager.getPeopleCardList().add(cardResult.card.get(i));
                                cardManager.getGachaCardList().add(cardResult.card.get(i));
                                break;
                            case "story" :
                                cardManager.getStoryCardList().add(cardResult.card.get(i));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CardResult> call, Throwable t) {
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
                }
            }

            @Override
            public void onFailure(Call<PlaceResult> call, Throwable t) {

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
                }
            }

            @Override
            public void onFailure(Call<OpenPeopleCard> call, Throwable t) {
                Log.d(TAG, t.getMessage());
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
                }
            }

            @Override
            public void onFailure(Call<OpenStoryCard> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

}
