package com.example.tourproject.CardBox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourproject.R;

import retrofit2.http.HEAD;

public class GridViewHolder extends RecyclerView.ViewHolder {

    public ImageView grid_card_image;
    public ImageView grid_card_lock;
    public ImageView grid_select;

    public GridViewHolder(View itemView) {
        super(itemView);

        grid_card_image = (ImageView) itemView.findViewById(R.id.grid_card_image);
    }
}
