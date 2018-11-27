package com.example.tourproject.Map;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tourproject.Util.Application;
import com.example.tourproject.R;
import com.example.tourproject.StoryPlay.StoryPlayActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Map2Data> verticalDatas;

    public VerticalAdapter(Context context, ArrayList<Map2Data> verticalDatas) {
        this.context = context;
        Collections.sort(verticalDatas, sortByPosition);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        VerticalAdapter.Map2ViewHolder holder = (VerticalAdapter.Map2ViewHolder) viewHolder;

        final Map2Data map2Data = verticalDatas.get(i);

        if(!map2Data.getMap2_image_url().equals("")) {
            Glide.with(context)
                    .load(Application.getInstance().getBaseImageUrl() + map2Data.getMap2_image_url())
                    .into(holder.horizon_icon);
        }

        holder.horizon_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verticalDatas.get(i).getMap2_state().equals("0")
                        || verticalDatas.get(i).getMap2_state().equals("1")) {
                    Intent intent = new Intent(context, StoryPlayActivity.class);
                    intent.putExtra("map2_id", map2Data.getMap2_id());
                    context.startActivity(intent);
                }
            }
        });
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
        ImageView horizon_arrow;

        public Map2ViewHolder(@NonNull final View itemView) {
            super(itemView);

            horizon_icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            horizon_arrow = (ImageView) itemView.findViewById(R.id.horizon_arrow);
        }
    }

    //Map2 데이터 순서 정렬
    private final static Comparator<Map2Data> sortByPosition = new Comparator<Map2Data>() {
        @Override
        public int compare(Map2Data o1, Map2Data o2) {
            return Integer.compare(o1.getMap2_position(), o2.getMap2_position());
        }
    };
}
