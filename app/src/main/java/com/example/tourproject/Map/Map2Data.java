package com.example.tourproject.Map;

public class Map2Data {
    int map2_id;
    int map1_id;
    String map2_state;
    String map2_image_url;

    @Override
    public String toString() {
        return "Map2Data{" +
                "map2_id=" + map2_id +
                ",\n map1_id=" + map1_id +
                ",\n map2_state='" + map2_state + '\'' +
                ",\n map2_image_url='" + map2_image_url + '\'' +
                "}\n\n";
    }
}
