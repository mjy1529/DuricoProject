package com.example.tourproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizonViewHolder2 extends RecyclerView.ViewHolder{
    public ImageView icon;
    public TextView description;

    public HorizonViewHolder2(View itemView) {
        super(itemView);

        icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
        description = (TextView) itemView.findViewById(R.id.horizon_title);
    }
}