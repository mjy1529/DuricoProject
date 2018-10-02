package com.example.tourproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder>{
    private ArrayList<GridData> gridDatas;

    public void setData(ArrayList<GridData> list){
        gridDatas = list;
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
        GridData data = gridDatas.get(position);

        holder.icon.setImageResource(data.getImg());
    }

    @Override
    public int getItemCount() {
        return gridDatas.size();
    }
}
