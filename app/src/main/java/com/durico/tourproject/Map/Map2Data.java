package com.durico.tourproject.Map;

public class Map2Data {
    private int map2_id;
    private int map1_id;
    private String map2_state;
    private String map2_image_url;
    private int map2_position;

    public int getMap2_id() {
        return map2_id;
    }

    public int getMap1_id() {
        return map1_id;
    }

    public String getMap2_state() {
        return map2_state;
    }

    public String getMap2_image_url() {
        return map2_image_url;
    }

    public int getMap2_position() {
        return map2_position;
    }

    public void setMap2_state(String map2_state) {
        this.map2_state = map2_state;
    }

    @Override
    public String toString() {
        return "Map2Data{" +
                "map2_id=" + map2_id +
                ", map1_id=" + map1_id +
                ", map2_state='" + map2_state + '\'' +
                ", map2_image_url='" + map2_image_url + '\'' +
                ", map2_position=" + map2_position +
                '}';
    }
}
