package com.durico.tourproject.StoryList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.durico.tourproject.R;

public class HorizonViewHolder extends RecyclerView.ViewHolder{

    public ImageButton icon;

    public HorizonViewHolder(View itemView) {
        super(itemView);

        icon = itemView.findViewById(R.id.horizon_icon);
    }
}
