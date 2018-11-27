package com.example.tourproject.Map;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.R;
import com.example.tourproject.StoryPlay.StoryPlayActivity;
import com.example.tourproject.Util.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

<<<<<<< HEAD
    private Context context;
    private ArrayList<Map2Data> verticalDatas;
=======
public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder>{
    private ArrayList<VerticalData> verticalDatas;
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043

    public VerticalAdapter(Context context, ArrayList<Map2Data> verticalDatas) {
        this.context = context;
        Collections.sort(verticalDatas, sortByPosition);
        this.verticalDatas = verticalDatas;
    }

    @Override
<<<<<<< HEAD
    public Map2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
=======
    public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recycler_items, parent, false);

<<<<<<< HEAD
        Map2ViewHolder holder = new Map2ViewHolder(view);
=======
        VerticalViewHolder holder = new VerticalViewHolder(view);

>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043
        return holder;
    }

    @Override
<<<<<<< HEAD
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
=======
    public void onBindViewHolder(VerticalViewHolder holder, int position) {
        VerticalData data = verticalDatas.get(position);

        holder.icon.setImageResource(data.getImg());
       if(data.getState() != 1 && data.getState() != 0) {
           ColorMatrix matrix = new ColorMatrix();
           matrix.setSaturation(0);
           ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
           holder.icon.setColorFilter(cf);
       }
       else{
          holder.icon.clearColorFilter();
          holder.icon.invalidate();
       }
       if(data.getId() != 3)
           holder.icon2.setBackgroundResource(data.getImg2());
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043
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
