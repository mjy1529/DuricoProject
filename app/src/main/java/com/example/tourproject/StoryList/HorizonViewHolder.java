package com.example.tourproject.StoryList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourproject.R;

public class HorizonViewHolder extends RecyclerView.ViewHolder{
    public ImageView icon;
    public TextView description;

    public HorizonViewHolder(View itemView) {
        super(itemView);

        icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
    }
}
