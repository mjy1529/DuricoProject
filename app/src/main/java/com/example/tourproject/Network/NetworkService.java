package com.example.tourproject.Network;

import com.example.tourproject.CardBox.CardResult;
import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.StoryList.StoryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("/user/insert_userId.php") //macAddress 넣기
    Call<String> insertUserId(@Query("user_id") String user_id);

    @GET("/story/get_story.php") //스토리 목록 가져오기
    Call<StoryResult> getStoryList();

//    @GET("/card/get_card.php") //전체 카드 가져오기
//    Call<CardResult> getTotalCard();

    @GET("/card/get_categoryCard.php") //카테고리 카드 가져오기
    Call<CardResult> getCategoryCard(@Query("category") String category);

    @GET("/story/get_map1.php") //map1 가져오기
    Call<Map1Result> getMap1List(@Query("story_id") String story_id);

    @GET("/story/get_allMap1.php") //전체 map1 가져오기
    Call<Map1Result> getAllMap1List();

    @GET("/story/get_map2.php") //map2 가져오기
    Call<Map2Result> getMap2List(@Query("map1_id") String map1_id);
}
