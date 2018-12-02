package com.durico.tourproject.Map;

import java.util.ArrayList;

public class Map1Data {
    String map_id;
    String story_id;
    String map1_title;
    String map1_image_url;
    double map1_mapx;
    double map1_mapy;

    ArrayList<Map2Data> map2List;

    public String getMap_id() {
        return map_id;
    }

    public String getStory_id() {
        return story_id;
    }

    public String getMap1_title() {
        return map1_title;
    }

    public String getMap1_image_url() {
        return map1_image_url;
    }

    public double getMap1_mapx() {
        return map1_mapx;
    }

    public double getMap1_mapy() {
        return map1_mapy;
    }

    public ArrayList<Map2Data> getMap2List() {
        return map2List;
    }

    public void setMap2List(ArrayList<Map2Data> map2List) {
        this.map2List = map2List;
    }

    @Override
    public String toString() {
        return "Map1Data{" +
                "map_id='" + map_id + '\'' +
                ", story_id='" + story_id + '\'' +
                ", map1_title='" + map1_title + '\'' +
                ", map1_image_url='" + map1_image_url + '\'' +
                ", map1_mapx=" + map1_mapx +
                ", map1_mapy=" + map1_mapy +
                ", map2List=" + map2List +
                '}';
    }
}
