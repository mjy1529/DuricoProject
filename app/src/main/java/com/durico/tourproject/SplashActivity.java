package com.durico.tourproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.durico.tourproject.CardBox.CardData;
import com.durico.tourproject.CardBox.CardResult;
import com.durico.tourproject.CardBox.OpenPeopleCard;
import com.durico.tourproject.CardBox.OpenStoryCard;
import com.durico.tourproject.CardBox.PlaceResult;
import com.durico.tourproject.Map.Map1Result;
import com.durico.tourproject.Map.Map2Data;
import com.durico.tourproject.Map.Map2Result;
import com.durico.tourproject.Map.UserMap2Result;
import com.durico.tourproject.Network.NetworkService;
import com.durico.tourproject.StoryList.StoryResult;
import com.durico.tourproject.StoryPlay.StoryPlayResult;
import com.durico.tourproject.Util.Application;
import com.durico.tourproject.Util.CardManager;
import com.durico.tourproject.Util.MapManager;
import com.durico.tourproject.Util.StoryListManager;
import com.durico.tourproject.Util.StoryPlayManager;
import com.durico.tourproject.Util.UserManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//background_splash.xml, styles.xml
public class SplashActivity extends AppCompatActivity {

    Handler handler;

    private UserManager userManager;
    private CardManager cardManager;
    private MapManager mapManager;
    private StoryListManager storyListManager;

    NetworkService networkService;

    public final static String TAG = "SPLASH";
    public static int state = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        state = 2;

        handler = new Handler();
        networkService = Application.getInstance().getNetworkService();

        userManager = UserManager.getInstance();
        cardManager = CardManager.getInstance();
        mapManager = MapManager.getInstance();
        storyListManager = StoryListManager.getInstance();

        if(checkInternet()) {
            setStoryListManager();
            getCardList();
            setMapDataManager();
            getUserMap2State(userManager.getUserId());

            getOpenPeopleCardIdx(userManager.getUserId());
            getOpenStoryCardIdx(userManager.getUserId());
            getPlaceCard(userManager.getUserId());
            getStoryPlayDataList();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SplashActivity.this, "서버와 통신중입니다.\n잠시만 기다려주세요 :)", Toast.LENGTH_LONG).show();
                }
            }, 4000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 9000);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInternetAlertDialog();
                }
            }, 3000);
        }
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
                        ActivityCompat.finishAffinity(SplashActivity.this);
                    }
                });
        alert.setCancelable(false);
        alert.show();
    }

    public void getCardList() {
        final ArrayList<CardData> peopleCardList = new ArrayList<>();
        final ArrayList<CardData> gachaCardList = new ArrayList<>();
        final ArrayList<CardData> storyCardList = new ArrayList<>();

        Call<CardResult> request = networkService.getCard();
        request.enqueue(new Callback<CardResult>() {
            @Override
            public void onResponse(Call<CardResult> call, Response<CardResult> response) {
                if(response.isSuccessful()) {
                    CardResult cardResult = response.body();
                    for(int i=0; i<cardResult.card.size(); i++) {
                        switch (cardResult.card.get(i).getCard_category()) {
                            case "people" :
                                //cardManager.getPeopleCardList().add(cardResult.card.get(i));
                                //cardManager.getGachaCardList().add(cardResult.card.get(i));
                                peopleCardList.add(cardResult.card.get(i));
                                gachaCardList.add(cardResult.card.get(i));
                                break;
                            case "story" :
                                //cardManager.getStoryCardList().add(cardResult.card.get(i));
                                storyCardList.add(cardResult.card.get(i));
                                break;
                        }
                    }
                    cardManager.setPeopleCardList(peopleCardList);
                    cardManager.setGachaCardList(gachaCardList);
                    cardManager.setStoryCardList(storyCardList);
                    Log.d(TAG, "PEOPLE, STORY CARD 받아오기 성공");

                }
            }

            @Override
            public void onFailure(Call<CardResult> call, Throwable t) {
                Log.d(TAG, "PEOPLE, STORY CARD 받아오기 실패");
            }
        });

        peopleCardList.clear();
        gachaCardList.clear();
        storyCardList.clear();
    }

    public void setStoryListManager() {
        Call<StoryResult> request = networkService.getStoryList();
        request.enqueue(new Callback<StoryResult>() {
            @Override
            public void onResponse(Call<StoryResult> call, Response<StoryResult> response) {
                if (response.isSuccessful()) {
                    StoryResult storyResult = response.body();
                    storyListManager.setStoryList(storyResult.story);
                    Log.d(TAG, "STORY LIST 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<StoryResult> call, Throwable t) {
                Log.d(TAG, "STORY LIST 받아오기 실패");
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
                Log.d(TAG, "STORY PLAY DATA 받아오기 실패 " + t.getMessage());
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
                Log.d(TAG, "PLACE CARD 받아오기 실패 " + t.getMessage());
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
                Log.d(TAG, "OPEN PEOPLE CARD LIST 받아오기 실패 " + t.getMessage());
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
                Log.d(TAG, "OPEN STORY CARD LIST 받아오기 실패 " + t.getMessage());
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
                Log.d(TAG, "MAP2 STATE 받아오기 실패 " + t.getMessage());
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
                    Log.d(TAG, "MAP 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<Map1Result> call, Throwable t) {
                Log.d(TAG, "MAP 받아오기 실패");
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

}
