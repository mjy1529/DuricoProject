package com.example.tourproject.Util;

import com.example.tourproject.Map.UserMap2Data;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserManager {

    private String userId; //사용자 맥주소
    private String user_card_url; //사용자 선택 카드
    private int open_people_card_cnt; //오픈된 인물카드 수
    private int place_card_cnt; //전체 장소카드 수
    private int open_story_card_cnt; //오픈된 스토리카드 수
    private ArrayList<Integer> openPeopleCardList; //오픈된 인물카드 인덱스
    private ArrayList<String> placeCardList; //장소카드 url
    private ArrayList<Integer> openStoryCardList; //오픈된 인물카드 인덱스
    private ArrayList<UserMap2Data> map2StateList; //오픈된 map2

    public static UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    public void initialize() {
        userId = getMACAddress("wlan0");
        openPeopleCardList = new ArrayList<>();
        placeCardList = new ArrayList<>();
        openStoryCardList = new ArrayList<>();

        map2StateList = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getUser_card_url() {
        return user_card_url;
    }

    public void setUser_card_url(String user_card_url) {
        this.user_card_url = user_card_url;
    }

    public int getOpen_people_card_cnt() {
        return this.openPeopleCardList.size();
    }

    public int getPlace_card_cnt() {
        return placeCardList.size();
    }

    public void setPlace_card_cnt(int place_card_cnt) {
        this.place_card_cnt = place_card_cnt;
    }

    public int getOpen_story_card_cnt() {
        return this.openStoryCardList.size();
    }

    public ArrayList<Integer> getOpenPeopleCardList() {
        return openPeopleCardList;
    }

    public void setOpenPeopleCardList(ArrayList<Integer> openPeopleCardList) {
        this.openPeopleCardList = openPeopleCardList;
    }

    public ArrayList<String> getPlaceCardList() {
        return placeCardList;
    }

    public void setPlaceCardList(ArrayList<String> placeCardList) {
        this.placeCardList = placeCardList;
    }

    public ArrayList<Integer> getOpenStoryCardList() {
        return openStoryCardList;
    }

    public void setOpenStoryCardList(ArrayList<Integer> openStoryCardList) {
        this.openStoryCardList = openStoryCardList;
    }

    public ArrayList<UserMap2Data> getMap2StateList() {
        return map2StateList;
    }

    public void setMap2StateList(ArrayList<UserMap2Data> map2StateList) {
        this.map2StateList = map2StateList;
    }

    //맥 주소 받아오기
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                if (interfaceName != null) {
                    if (!networkInterface.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++) {
                    buf.append(String.format("%02X:", mac[idx]));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
