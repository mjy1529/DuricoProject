package com.example.tourproject.CardBox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tourproject.AppUtility.Application;
import com.example.tourproject.R;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder>{
    private Context context;
    private ArrayList<CardData> cardDataList;

    public GridAdapter(Context context, ArrayList<CardData> cardDataList) {
        this.context = context;
        this.cardDataList = cardDataList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_recycler_items, parent, false);

        GridViewHolder holder = new GridViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        CardData data = cardDataList.get(position);
        Glide.with(context)
                .load(Application.getInstance().getBaseImageUrl() + data.getCard_image_url())
                .into(holder.grid_card_image);
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }
}
