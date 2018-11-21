package com.example.tourproject.Map;

public class Map1Data {
    int map_id;
    int story_id;
    String map1_title;
    String map1_image_url;
    double map1_mapx;
    double map1_mapy;

    @Override
    public String toString() {
        return "Map1Data{" +
                "map_id=" + map_id +
                ", story_id=" + story_id +
                ", map1_title='" + map1_title + '\'' +
                ", map1_image_url='" + map1_image_url + '\'' +
                ", map1_mapx=" + map1_mapx +
                ", map2_mapy=" + map1_mapy +
                '}';
    }
}
