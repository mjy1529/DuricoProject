package com.example.tourproject.CardBox;

public class CardData {
    private String card_id;
    private String card_description;
    private String card_image_url;
    private String card_category;

    public String getCard_id() {
        return card_id;
    }

    public String getCard_description() {
        return card_description;
    }

    public String getCard_image_url() {
        return card_image_url;
    }

    public String getCard_category() {
        return card_category;
    }

    @Override
    public String toString() {
        return "CardData{" +
                "card_id='" + card_id + '\'' +
                ", card_description='" + card_description + '\'' +
                ", card_image_url='" + card_image_url + '\'' +
                ", card_category='" + card_category + '\'' +
                '}';
    }
}
