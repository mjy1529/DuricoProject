package com.example.tourproject.CardBox;

import android.os.AsyncTask;
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

import com.example.tourproject.AppUtility.Application;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class FirstCardFragment extends Fragment {

    TextView card_number;
    RecyclerView card_recyclerView;

    ArrayList<CardData> cardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_card, container, false);
        card_number = (TextView) view.findViewById(R.id.card_number);
        card_recyclerView = (RecyclerView) view.findViewById(R.id.card_recyclerView);

        getCategoryCard("people");
        return view;
    }

    public void getCategoryCard(final String category) {
        final NetworkService networkService = Application.getInstance().getNetworkService();

        new AsyncTask<Void, Void, String>() {
            CardResult cardResult;
            @Override
            protected String doInBackground(Void... voids) {
                Call<CardResult> request = networkService.getCategoryCard(category);
                try {
                    cardResult = request.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                cardList = cardResult.card;

                GridAdapter adapter = new GridAdapter(getContext(), cardList);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

                card_recyclerView.setLayoutManager(layoutManager);
                card_recyclerView.setAdapter(adapter);

                int openCardCnt = 3;
                card_number.setText(openCardCnt + " / " + adapter.getItemCount());
            }
        }.execute();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
