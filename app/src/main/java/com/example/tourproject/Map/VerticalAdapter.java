package com.example.tourproject.Map;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.R;
import com.example.tourproject.StoryList.HorizonViewHolder;
import com.example.tourproject.Util.Application;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Map2Data> verticalDatas;

    public VerticalAdapter(Context context, ArrayList<Map2Data> verticalDatas) {
        this.context = context;
        this.verticalDatas = verticalDatas;
    }

    @Override
    public Map2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recycler_items, parent, false);

        Map2ViewHolder holder = new Map2ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VerticalAdapter.Map2ViewHolder holder = (VerticalAdapter.Map2ViewHolder) viewHolder;

        Map2Data map2Data = verticalDatas.get(i);
        Glide.with(context)
                .load(Application.getInstance().getBaseImageUrl() + map2Data.map2_image_url)
                .into(holder.horizon_icon);
    }

//    @Override
//    public void onBindViewHolder(HorizonViewHolder holder, int position) {
//        VerticalData data = verticalDatas.get(position);
//
//        holder.icon.setImageResource(data.getImg());
//        if (data.getState() != 1 && data.getState() != 0) {
//            ColorMatrix matrix = new ColorMatrix();
//            matrix.setSaturation(0);
//            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
//            holder.icon.setColorFilter(cf);
//        } else {
//            holder.icon.clearColorFilter();
//            holder.icon.invalidate();
//        }
//    }

    @Override
    public int getItemCount() {
        return verticalDatas.size();
    }

    public class Map2ViewHolder extends RecyclerView.ViewHolder {

        ImageView horizon_icon;
        TextView horizon_title;

        public Map2ViewHolder(@NonNull View itemView) {
            super(itemView);

            horizon_icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            horizon_title = (TextView) itemView.findViewById(R.id.horizon_title);
        }
    }
}
