package com.example.tourproject.Map;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourproject.R;

public class VerticalViewHolder extends RecyclerView.ViewHolder{
    public ImageView icon;
    public ImageView icon2;

    public VerticalViewHolder(View itemView) {
        super(itemView);

        icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
        icon2 = (ImageView) itemView.findViewById(R.id.horizon_arrow);
    }
}
