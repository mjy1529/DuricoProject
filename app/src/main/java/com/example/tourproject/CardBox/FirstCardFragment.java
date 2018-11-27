package com.example.tourproject.CardBox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourproject.Util.Application;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Util.CardManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstCardFragment extends Fragment {

    TextView card_number;
    RecyclerView card_recyclerView;

    ArrayList<CardData> cardList;
    public static final String TAG = "FirstCardFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_card, container, false);
        card_number = (TextView) view.findViewById(R.id.card_number);
        card_recyclerView = (RecyclerView) view.findViewById(R.id.card_recyclerView);

        setPlaceCard();
        return view;
    }

    public void setPlaceCard() {
        cardList = CardManager.getInstance().getPlaceCardList(); //장소카드
        ArrayList<Integer> openCardList = UserManager.getInstance().getOpenPlaceCardList();

        GridAdapter adapter = new GridAdapter(getContext(), cardList, openCardList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        card_recyclerView.setLayoutManager(layoutManager);
        card_recyclerView.setAdapter(adapter);

        int openCardCnt = UserManager.getInstance().getOpen_place_card_cnt();
        card_number.setText(openCardCnt + " / " + adapter.getItemCount());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
