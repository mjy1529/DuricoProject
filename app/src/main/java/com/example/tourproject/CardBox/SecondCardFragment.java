package com.example.tourproject.CardBox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourproject.R;
import com.example.tourproject.Util.CardManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

public class SecondCardFragment extends Fragment {

    TextView card_number;
    RecyclerView card_recyclerView;

    ArrayList<CardData> cardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_card, container, false);
        card_number = (TextView) view.findViewById(R.id.card_number);
        card_recyclerView = (RecyclerView) view.findViewById(R.id.card_recyclerView);

        setStoryCard();

        return view;
    }

    public void setStoryCard() {
        cardList = CardManager.getInstance().getStoryCardList(); //스토리카드
        ArrayList<Integer> openCardList = UserManager.getInstance().getOpenStoryCardList();

        GridAdapter adapter = new GridAdapter(getContext(), cardList, openCardList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        card_recyclerView.setLayoutManager(layoutManager);
        card_recyclerView.setAdapter(adapter);

        int openCardCnt = UserManager.getInstance().getOpen_story_card_cnt();
        card_number.setText(String.valueOf(openCardCnt));
    }
}
