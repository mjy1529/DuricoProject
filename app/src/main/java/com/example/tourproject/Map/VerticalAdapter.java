package com.example.tourproject.Map;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tourproject.R;
import com.example.tourproject.StoryList.HorizonViewHolder;

import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<HorizonViewHolder>{
    private ArrayList<VerticalData> verticalDatas;

    public void setData(ArrayList<VerticalData> list){
        verticalDatas = list;
    }

    @Override
    public HorizonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recycler_items, parent, false);

            HorizonViewHolder holder = new HorizonViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(HorizonViewHolder holder, int position) {
        VerticalData data = verticalDatas.get(position);

        holder.description.setText(data.getText());
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
    }

    @Override
    public int getItemCount() {
        return verticalDatas.size();
    }
}
