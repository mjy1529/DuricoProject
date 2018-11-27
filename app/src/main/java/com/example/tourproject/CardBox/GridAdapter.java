package com.example.tourproject.CardBox;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourproject.MainActivity;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.Util.Application;
import com.example.tourproject.R;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {

    private Context context;
    private ArrayList<CardData> cardDataList;
    private ArrayList<Integer> openCardList;
    private CardData data;
    GridViewHolder holder;

    int s;

    public static final String TAG = "Card Grid Adapter";

    public GridAdapter(Context context, ArrayList<CardData> cardDataList, ArrayList<Integer> openCardList) {
        this.context = context;
        this.cardDataList = cardDataList;
        this.openCardList = openCardList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_recycler_items, parent, false);

        holder = new GridViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, final int position) {
        data = cardDataList.get(position);
        GradientDrawable drawable=
                (GradientDrawable) context.getDrawable(R.drawable.locklayout);
        boolean isOpen = false;
        for(int i=0; i<openCardList.size(); i++) {
            if(openCardList.get(i) == data.getCard_idx()) {
                isOpen = true;
                break;
            } else {
                isOpen = false;
            }
        }

        if(isOpen) { //카드가 오픈되어 있을 때만 카드 이미지 확인 및 클릭 가능
            holder.grid_card_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(context)
                    .load(Application.getInstance().getBaseImageUrl() + data.getCard_image_url())
                    .into(holder.grid_card_image);

            if(data.getCard_image_url().equals(UserManager.getInstance().getUser_card_url())) {
                holder.grid_select.setVisibility(View.VISIBLE);
                s = data.getCard_idx();
            }
            else {
                holder.grid_select.setVisibility(View.INVISIBLE);
            }
            //카드를 클릭했을 때의 이벤트
            holder.grid_card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProfileDialog(cardDataList.get(position)); //메인사진 변경 다이얼로그 띄움
                }
            });
        } else {
            //holder.grid_card_image.setBackgroundResource(R.drawable.lock);
            holder.grid_card_image.setScaleType(ImageView.ScaleType.CENTER);
            Glide.with(context).load(R.drawable.lock).into(holder.grid_card_image);
            holder.grid_select.setVisibility(View.INVISIBLE);
        }
        holder.grid_card_image.setBackground(drawable);
        holder.grid_card_image.setClipToOutline(true);

    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    public void showProfileDialog(final CardData data) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("프로필 사진 설정")
                .setMessage("프로필 사진으로 설정하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserCard(data);
                        dialog.dismiss();
                        ((MainActivity) MainActivity.mContext).changeProfileImage(data.getCard_image_url());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alert.setCancelable(false);
        alert.show();
    }

    public void updateUserCard(CardData data) {
        NetworkService networkService = Application.getInstance().getNetworkService();
        Call<String> request
                = networkService.updateMainCard(UserManager.getInstance().getUserId(), data.getCard_image_url());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "실패!!");
            }
        });

        UserManager.getInstance().setUser_card_url(data.getCard_image_url());
    }
}
