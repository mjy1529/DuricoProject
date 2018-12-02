package com.durico.tourproject.Util;

import com.durico.tourproject.Map.Map1Data;

import java.util.ArrayList;

public class MapManager {

    private ArrayList<Map1Data> mapList;

    public static MapManager instance = new MapManager();

    public static MapManager getInstance() {
        return instance;
    }

    public void initialize() {
        mapList = new ArrayList<>();
    }

    public ArrayList<Map1Data> getMapList() {
        return mapList;
    }

    public void setMapList(ArrayList<Map1Data> mapList) {
        this.mapList = mapList;
    }
}
