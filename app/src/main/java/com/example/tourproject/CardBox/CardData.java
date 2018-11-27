package com.example.tourproject.CardBox;

public class CardData {
    private int card_idx;
    private String card_name;
    private String card_image_url;
    private String card_category;
    private String card_description;
    private int gacha;
    private int map2_id;

    public String getCard_image_url() {
        return card_image_url;
    }

    public String getCard_category() {
        return card_category;
    }

    public int getCard_idx() {
        return card_idx;
    }

    public String getCard_name() {
        return card_name;
    }

    public String getCard_description() {
        return card_description;
    }

    public int getGacha() {
        return gacha;
    }

    public int getMap2_id() {
        return map2_id;
    }

    @Override
    public String toString() {
        return "CardData{" +
                "card_idx=" + card_idx +
                ", card_name='" + card_name + '\'' +
                ", card_image_url='" + card_image_url + '\'' +
                ", card_category='" + card_category + '\'' +
                ", card_description='" + card_description + '\'' +
                ", gacha=" + gacha +
                '}';
    }
}
