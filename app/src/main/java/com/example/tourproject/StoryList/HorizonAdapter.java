package com.example.tourproject.StoryList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tourproject.Map.MapActivity;
import com.example.tourproject.Util.Application;
import com.example.tourproject.R;

import java.util.ArrayList;

public class HorizonAdapter extends RecyclerView.Adapter<HorizonViewHolder> {

    private Context context;
    private ArrayList<StoryData> storyList;

    public HorizonAdapter(Context context, ArrayList<StoryData> storyList) {
        this.context = context;
        this.storyList = storyList;
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
        final StoryData data = storyList.get(position);

        Glide.with(context)
                .load(Application.getInstance().getBaseImageUrl() + data.getStory_image_url())
                .into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("story_id", String.valueOf(data.getStory_id()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }
}
