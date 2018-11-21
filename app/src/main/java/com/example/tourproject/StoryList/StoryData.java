package com.example.tourproject.StoryList;

public class StoryData {
    private int story_id;
    private String story_title;
    private String story_image_url;

    public int getStory_id() {
        return story_id;
    }

    public String getStory_title() {
        return story_title;
    }

    public String getStory_image_url() {
        return story_image_url;
    }

    @Override
    public String toString() {
        return "StoryData{" +
                "story_id=" + story_id +
                ", story_title='" + story_title + '\'' +
                ", story_image_url='" + story_image_url + '\'' +
                '}';
    }
}
