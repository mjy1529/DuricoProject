package com.example.tourproject.CardBox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourproject.R;

public class GridViewHolder extends RecyclerView.ViewHolder {

    public ImageView grid_card_image;

    public GridViewHolder(View itemView) {
        super(itemView);

        grid_card_image = (ImageView) itemView.findViewById(R.id.grid_card_image);
    }
}