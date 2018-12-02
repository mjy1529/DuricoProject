package com.durico.tourproject.Util;

import com.durico.tourproject.StoryPlay.StoryPlayData;

import java.util.ArrayList;

public class StoryPlayManager {

    private ArrayList<StoryPlayData> storyPlayList;

    public static StoryPlayManager instance = new StoryPlayManager();

    public static StoryPlayManager getInstance() {
        return instance;
    }

    public void initialize() {
        storyPlayList = new ArrayList<>();
    }

    public ArrayList<StoryPlayData> getStoryPlayList() {
        return storyPlayList;
    }

    public void setStoryPlayList(ArrayList<StoryPlayData> storyPlayList) {
        this.storyPlayList = storyPlayList;
    }
}
