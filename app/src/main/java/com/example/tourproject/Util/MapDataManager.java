package com.example.tourproject.Util;

import com.example.tourproject.Map.Map1Data;
import com.example.tourproject.Map.Map2Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapDataManager {

    private ArrayList<Map1Data> mapList;

    public static MapDataManager instance = new MapDataManager();

    public static MapDataManager getInstance() {
        return instance;
    }

    public void initialize() {
        mapList = new ArrayList<>();
    }

    public ArrayList<Map1Data> getMapList() {
        return mapList;
    }
}
