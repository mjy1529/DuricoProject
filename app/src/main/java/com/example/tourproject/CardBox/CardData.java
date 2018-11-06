package com.example.tourproject.CardBox;

public class CardData {
    private int card_image;
    private String card_name;

    public CardData(int card_image) {
        this.card_image = card_image;
    }

    public CardData(int card_image, String card_name) {
        this.card_image = card_image;
        this.card_name = card_name;
    }

    public int getCard_image() {
        return card_image;
    }

    public String getCard_name() {
        return card_name;
    }
}
