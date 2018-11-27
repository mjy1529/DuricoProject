package com.example.tourproject.Util;

import android.util.Log;

import com.example.tourproject.CardBox.CardResult;
import com.example.tourproject.CardBox.OpenPeopleCard;
import com.example.tourproject.CardBox.OpenPlaceCard;
import com.example.tourproject.CardBox.OpenStoryCard;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.StoryPlay.StoryPlayResult;

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

        //상세이야기, 전체 카드 받아오기
        getStoryPlayDataList();
        getCardList();

        userManager = UserManager.getInstance();
        userManager.initialize();

        getOpenPeopleCardIdx(userManager.getUserId());
        getOpenPlaceCardIdx(userManager.getUserId());
        getOpenStoryCardIdx(userManager.getUserId());
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
        final CardManager cardManager = CardManager.getInstance();
        cardManager.initialize();

        Call<CardResult> request = networkService.getAllCard();
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
                            case "place" :
                                cardManager.getPlaceCardList().add(cardResult.card.get(i));
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

    public void getOpenPeopleCardIdx(String user_id) {
        Call<OpenPeopleCard> request = networkService.getOpenPeopleCard(user_id);
        request.enqueue(new Callback<OpenPeopleCard>() {
            @Override
            public void onResponse(Call<OpenPeopleCard> call, Response<OpenPeopleCard> response) {
                if(response.isSuccessful()) {
                    OpenPeopleCard openPeopleCard = response.body();
                    userManager.setOpenPeopleCardList(openPeopleCard.openCardList);
                    userManager.setOpen_people_card_cnt(openPeopleCard.openCardList.size());
                }
            }

            @Override
            public void onFailure(Call<OpenPeopleCard> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public void getOpenPlaceCardIdx(String user_id) {
        Call<OpenPlaceCard> request = networkService.getOpenPlaceCard(user_id);
        request.enqueue(new Callback<OpenPlaceCard>() {
            @Override
            public void onResponse(Call<OpenPlaceCard> call, Response<OpenPlaceCard> response) {
                if(response.isSuccessful()) {
                    OpenPlaceCard openPlaceCard = response.body();
                    userManager.setOpenPlaceCardList(openPlaceCard.openCardList);
                    userManager.setOpen_place_card_cnt(openPlaceCard.openCardList.size());
                }
            }

            @Override
            public void onFailure(Call<OpenPlaceCard> call, Throwable t) {
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
                    userManager.setOpen_story_card_cnt(openStoryCard.openCardList.size());
                }
            }

            @Override
            public void onFailure(Call<OpenStoryCard> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

}
