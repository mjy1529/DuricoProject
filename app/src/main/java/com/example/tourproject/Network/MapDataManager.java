package com.example.tourproject.Network;

import com.example.tourproject.Map.Map1Data;
import com.example.tourproject.Map.Map2Data;

import java.util.HashMap;
import java.util.Map;

public class MapDataManager {

    private Map<Integer, Map1Data> map1List;
    private Map<Integer, Map2Data> map2List;
    public static MapDataManager instance = new MapDataManager();

    public static MapDataManager getInstance() {
        return instance;
    }

    public void initialize() {
        map1List = new HashMap<>();
        map2List = new HashMap<>();
    }

    public Map<Integer, Map1Data> getMap1List() {
        return map1List;
    }

    public Map<Integer, Map2Data> getMap2List() {
        return map2List;
    }
}
