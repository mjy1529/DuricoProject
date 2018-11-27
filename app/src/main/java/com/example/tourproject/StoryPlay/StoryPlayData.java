package com.example.tourproject.StoryPlay;

public class StoryPlayData {

    private int play_id;
    private int map2_id;
    private String play_content;
    private String play_image_url;
    private String person_name;

    public int getPlay_id() {
        return play_id;
    }

    public int getMap2_id() {
        return map2_id;
    }

    public String getPlay_content() {
        return play_content;
    }

    public String getPlay_image_url() {
        return play_image_url;
    }

    public String getPerson_name() {
        return person_name;
    }

    @Override
    public String toString() {
        return "StoryPlayData{" +
                "play_id='" + play_id + '\'' +
                ", map2_id='" + map2_id + '\'' +
                ", play_content='" + play_content + '\'' +
                ", play_image_url='" + play_image_url + '\'' +
                ", person_name='" + person_name + '\'' +
                '}';
    }
}
