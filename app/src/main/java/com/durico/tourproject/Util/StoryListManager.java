package com.durico.tourproject.Util;
import com.durico.tourproject.StoryList.StoryData;

import java.util.ArrayList;

public class StoryListManager {

    private ArrayList<StoryData> storyList;

    public static StoryListManager instance = new StoryListManager();

    public static StoryListManager getInstance() {
        return instance;
    }

    public void initialize() {
        storyList = new ArrayList<>();
    }

    public ArrayList<StoryData> getStoryList() {
        return storyList;
    }

    public void setStoryList(ArrayList<StoryData> storyList) {
        this.storyList = storyList;
    }
}
