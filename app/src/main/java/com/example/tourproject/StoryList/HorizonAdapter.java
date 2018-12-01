package com.example.tourproject.StoryList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourproject.Map.MapActivity;
import com.example.tourproject.Util.Application;
import com.example.tourproject.R;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

public class HorizonAdapter extends RecyclerView.Adapter<HorizonViewHolder>{

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
    public void onBindViewHolder(final HorizonViewHolder holder, final int position) {
        final StoryData data = storyList.get(position);

        Glide.with(context)
                .load(Application.getInstance().getBaseImageUrl() + data.getStory_image_url())
                .into(holder.icon);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("이건 안나오나??", "눈누");
                //Context context = v.getContext();
                //Toast.makeText(context, position +"", Toast.LENGTH_LONG).show();
                int buttonPushImage = R.drawable.storylistbutton1;
                switch (position) {
                    case 0:
                        buttonPushImage = R.drawable.storylistbutton1;
                        break;
                    case 1:
                        buttonPushImage = R.drawable.storylistbutton2;
                        break;
                    case 2:
                        buttonPushImage = R.drawable.storylistbutton3;
                        break;
                    case 3:
                        buttonPushImage = R.drawable.storylistbutton4;
                        break;
                }
                Glide.with(context).load(buttonPushImage).into(holder.icon);
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("story_id", String.valueOf(data.getStory_id()));
                context.startActivity(intent);
                notifyItemChanged(position);
            }
        });

        /*
        holder.icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int buttonPushImage = R.drawable.storylistbutton1;
                switch (position) {
                    case 0:
                        buttonPushImage = R.drawable.storylistbutton1;
                        break;
                    case 1:
                        buttonPushImage = R.drawable.storylistbutton2;
                        break;
                    case 2:
                        buttonPushImage = R.drawable.storylistbutton3;
                        break;
                    case 3:
                        buttonPushImage = R.drawable.storylistbutton4;
                        break;
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN ://버튼이 눌렸을 때
                        Glide.with(context).load(buttonPushImage).into(holder.icon);
                        break;
                    case MotionEvent.ACTION_UP :  //손이 떼어졌을 때
                        Glide.with(context)
                                .load(Application.getInstance().getBaseImageUrl() + data.getStory_image_url())
                                .into(holder.icon);
                        Intent intent = new Intent(context, MapActivity.class);
                        intent.putExtra("story_id", String.valueOf(data.getStory_id()));
                        context.startActivity(intent);
                        break;
                }
                return true;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

}
