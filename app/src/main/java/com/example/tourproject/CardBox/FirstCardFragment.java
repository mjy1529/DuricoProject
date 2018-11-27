package com.example.tourproject.CardBox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourproject.R;

import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

public class FirstCardFragment extends Fragment {

    TextView card_number;
    RecyclerView card_recyclerView;

    ArrayList<String> placeCardList;
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
        placeCardList = UserManager.getInstance().getPlaceCardList();

        GridAdapter adapter = new GridAdapter(getContext(), placeCardList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        card_recyclerView.setLayoutManager(layoutManager);
        card_recyclerView.setAdapter(adapter);

        card_number.setText(String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
