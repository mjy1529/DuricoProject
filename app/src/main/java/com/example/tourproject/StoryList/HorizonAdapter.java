package com.example.tourproject.StoryList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourproject.R;

import java.util.ArrayList;

public class HorizonAdapter extends RecyclerView.Adapter<HorizonViewHolder>{
    private ArrayList<HorizonData> horizonDatas;

    public void setData(ArrayList<HorizonData> list){
        horizonDatas = list;
    }

    @Override
    public HorizonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizon_recycler_items, parent, false);

            HorizonViewHolder holder = new HorizonViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(HorizonViewHolder holder, int position) {
        HorizonData data = horizonDatas.get(position);

        //holder.description.setText(data.getText());
        holder.icon.setImageResource(data.getImg());
    }

    @Override
    public int getItemCount() {
        return horizonDatas.size();
    }
}
