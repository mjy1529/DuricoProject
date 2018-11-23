package com.example.tourproject.Map;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tourproject.R;
import com.example.tourproject.StoryList.HorizonViewHolder;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder>{
    private ArrayList<VerticalData> verticalDatas;

    public void setData(ArrayList<VerticalData> list){
        verticalDatas = list;
    }

    @Override
    public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recycler_items, parent, false);

        VerticalViewHolder holder = new VerticalViewHolder(view);

        return holder;
    }

    @Override
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
    }

    @Override
    public int getItemCount() {
        return verticalDatas.size();
    }
}
