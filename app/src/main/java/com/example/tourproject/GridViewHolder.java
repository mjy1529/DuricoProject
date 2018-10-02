package com.example.tourproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewHolder extends RecyclerView.ViewHolder{
    public ImageView icon;

    public GridViewHolder(View itemView) {
        super(itemView);

        icon = (ImageView) itemView.findViewById(R.id.grid_item);
    }
}
