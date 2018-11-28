package com.example.tourproject.Network;

import com.example.tourproject.CardBox.CardResult;
import com.example.tourproject.CardBox.PlaceResult;
import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.CardBox.OpenPeopleCard;
import com.example.tourproject.CardBox.OpenStoryCard;
import com.example.tourproject.Map.UserMap2Result;
import com.example.tourproject.StoryList.StoryResult;
import com.example.tourproject.StoryPlay.StoryPlayResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("/user/insert_user.php") //사용자 정보 넣기
    Call<String> insertUser(@Query("user_id") String user_id);

    @GET("/user/update_main_card.php") //메인 사진 지정
    Call<String> updateMainCard(@Query("user_id") String user_id, @Query("user_card_url") String user_card_url);

    @GET("/user/get_open_people_card.php") //오픈 인물카드 인덱스 가져오기
    Call<OpenPeopleCard> getOpenPeopleCard(@Query("user_id") String user_id);

    @GET("/user/get_open_story_card.php") //오픈 스토리카드 인덱스 가져오기
    Call<OpenStoryCard> getOpenStoryCard(@Query("user_id") String user_id);

    @GET("/user/update_people_card.php") //인물 카드 오픈하기
    Call<String> updatePeopleCardState(@Query("user_id") String user_id, @Query("people_card_idx") int people_card_idx);

    @GET("/user/update_story_card.php") //스토리 카드 오픈하기
    Call<String> updateStoryCardState(@Query("user_id") String user_id, @Query("story_card_idx") int story_card_idx);

    @GET("/card/get_card.php") //전체 카드 가져오기
    Call<CardResult> getCard();

//    @GET("/card/get_categoryCard.php") //카테고리 카드 가져오기
//    Call<CardResult> getCategoryCard(@Query("category") String category);

    @GET("/story/get_story.php") //스토리 목록 가져오기
    Call<StoryResult> getStoryList();

//    @GET("/story/get_map1.php") //map1 가져오기
//    Call<Map1Result> getMap1List(@Query("story_id") String story_id);

    @GET("/story/get_allMap1.php") //전체 map1 가져오기
    Call<Map1Result> getAllMap1List();

    @GET("/story/get_map2.php") //map2 가져오기
    Call<Map2Result> getMap2List(@Query("map1_id") String map1_id);

//    @GET("/story/get_story_play.php") //상세이야기 가져오기
//    Call<StoryPlayResult> getStoryPlayList (@Query("map2_id") String map2_id);

    @GET("/story/get_all_storyPlay.php") //모든 상세이야기 가져오기
    Call<StoryPlayResult> getStoryPlayList();

    @GET("/user/get_map2State.php") //사용자별 map2 상태 가져오기
    Call<UserMap2Result> getMap2State(@Query("user_id") String user_id);

    @GET("/user/update_map2.php") //map2 상태 업데이트
    Call<String> updateMap2State(@Query("user_id") String user_id, @Query("map2_id") int map2_id);

    @GET("/card/insert_placeCard.php") //장소카드 넣기
    Call<String> insertPlaceCard(@Query("user_id") String user_id, @Query("place_card_url") String place_card_url);

    @GET("/card/get_placeCard.php") //장소카드 가져오기
    Call<PlaceResult> getPlaceCard(@Query("user_id") String user_id);
}
