package com.example.tourproject.CardBox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourproject.R;

import java.util.ArrayList;


public class FirstCardFragment extends Fragment {
    TextView card_number;

    RecyclerView card_recyclerView;
    GridAdapter adapter;
    GridLayoutManager layoutManager;

    ArrayList<CardData> cardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_card, container, false);
        card_number = (TextView) view.findViewById(R.id.card_number);
        card_recyclerView = (RecyclerView) view.findViewById(R.id.card_recyclerView);

        setCardList();

        adapter = new GridAdapter(getContext(), cardList);
        layoutManager = new GridLayoutManager(getContext(), 3);

        card_recyclerView.setLayoutManager(layoutManager);
        card_recyclerView.setAdapter(adapter);

        int openCardCnt = 3;
        card_number.setText(openCardCnt + " / " + adapter.getItemCount());
        return view;
    }

    public void setCardList() {
        cardList = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            cardList.add(new CardData(R.drawable.namhan));
        }
    }
}
