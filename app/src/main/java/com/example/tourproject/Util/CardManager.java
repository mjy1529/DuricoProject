package com.example.tourproject.Util;

import com.example.tourproject.CardBox.CardData;

import java.util.ArrayList;

public class CardManager {

    private ArrayList<CardData> peopleCardList;
//    private ArrayList<CardData> placeCardList;
    private ArrayList<CardData> storyCardList;

    private ArrayList<CardData> gachaCardList;

    private static CardManager instance = new CardManager();

    public static CardManager getInstance() {
        return instance;
    }

    public void initialize() {
        peopleCardList = new ArrayList<>();
//        placeCardList = new ArrayList<>();
        storyCardList = new ArrayList<>();

        gachaCardList = new ArrayList<>();
    }

    public ArrayList<CardData> getPeopleCardList() {
        return peopleCardList;
    }

    public void setPeopleCardList(ArrayList<CardData> peopleCardList) {
        this.peopleCardList = peopleCardList;
    }

//    public ArrayList<CardData> getPlaceCardList() {
//        return placeCardList;
//    }
//
//    public void setPlaceCardList(ArrayList<CardData> placeCardList) {
//        this.placeCardList = placeCardList;
//    }

    public ArrayList<CardData> getStoryCardList() {
        return storyCardList;
    }

    public void setStoryCardList(ArrayList<CardData> storyCardList) {
        this.storyCardList = storyCardList;
    }

    public ArrayList<CardData> getGachaCardList() {
        return gachaCardList;
    }

    public void setGachaCardList(ArrayList<CardData> gachaCardList) {
        this.gachaCardList = gachaCardList;
    }
}
