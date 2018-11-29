package com.example.tourproject.CardBox;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourproject.MainActivity;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.StoryPlay.StoryPlayActivity;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {

    private Context context;
    private ArrayList<CardData> cardDataList;
    private ArrayList<Integer> openCardList;
    private ArrayList<String> placeCardList;
    private CardData data;
    GridViewHolder holder;
    Dialog magnifyCardDialog;

    int s, p;

    public static final String TAG = "Card Grid Adapter";

    public GridAdapter(Context context, ArrayList<String> placeCardList, int p) {
        this.context = context;
        this.placeCardList = placeCardList;
        this.p = p;
    }

    public GridAdapter(Context context, ArrayList<CardData> cardDataList, ArrayList<Integer> openCardList, int p) {
        this.context = context;
        this.cardDataList = cardDataList;
        this.openCardList = openCardList;
        this.p = p;
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
        if (openCardList != null) {

            data = cardDataList.get(position);

            GradientDrawable drawable =
                    (GradientDrawable) context.getDrawable(R.drawable.locklayout);

            boolean isOpen = false;
            for (int i = 0; i < openCardList.size(); i++) {
                if (openCardList.get(i) == data.getCard_idx()) {
                    isOpen = true;
                    break;
                } else {
                    isOpen = false;
                }
            }

            if (isOpen) { //카드가 오픈되어 있을 때만 카드 이미지 확인 및 클릭 가능
                holder.grid_card_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Glide.with(context)
                        .load(Application.getInstance().getBaseImageUrl() + data.getCard_image_url())
                        .into(holder.grid_card_image);

                if (data.getCard_image_url().equals(UserManager.getInstance().getUser_card_url())) {
                    if(p == 0)
                        holder.grid_select.setBackgroundResource(R.drawable.selected_2);
                    else if(p == 1)
                        holder.grid_select.setBackgroundResource(R.drawable.selected_3);
                    else
                        holder.grid_select.setBackgroundResource(R.drawable.selected);
                    holder.grid_select.setVisibility(View.VISIBLE);
                    s = data.getCard_idx();
                } else {
                    holder.grid_select.setVisibility(View.INVISIBLE);
                }

                //카드를 클릭했을 때의 이벤트
                holder.grid_card_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cardDataList.get(position).getCard_category().equals("people")) {
                            showProfileDialog(cardDataList.get(position)); //메인사진 변경 다이얼로그 띄움
                        } else if (cardDataList.get(position).getCard_category().equals("story")){
                            magnifyCard(cardDataList.get(position), null);
                        }
                    }
                });

            } else {
                //holder.grid_card_image.setBackgroundResource(R.drawable.lock);
                holder.grid_card_image.setScaleType(ImageView.ScaleType.CENTER);
                if(p == 0)
                    Glide.with(context).load(R.drawable.lock_2).into(holder.grid_card_image);
                else if(p == 1)
                    Glide.with(context).load(R.drawable.lock_3).into(holder.grid_card_image);
                else
                    Glide.with(context).load(R.drawable.lock).into(holder.grid_card_image);
                holder.grid_select.setVisibility(View.INVISIBLE);
            }

            holder.grid_card_image.setBackground(drawable);
            holder.grid_card_image.setClipToOutline(true);

        } else {
            Glide.with(context).load(placeCardList.get(position)).into(holder.grid_card_image);
            holder.grid_card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    magnifyCard(null, placeCardList.get(position));
                }
            });

            GradientDrawable drawable =
                    (GradientDrawable) context.getDrawable(R.drawable.locklayout2);
            holder.grid_card_image.setBackground(drawable);
            holder.grid_card_image.setClipToOutline(true);
        }
    }

    @Override
    public int getItemCount() {
        if (openCardList != null) {
            return cardDataList.size();
        } else {
            return placeCardList.size();
        }
    }

    public void showProfileDialog(final CardData data) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("프로필 사진 설정")
                .setMessage("프로필 사진으로 설정하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserCard(data);

                        Log.d("그리드 어댑터", data.toString());

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

    public void magnifyCard(CardData cardData, String placeURL) {
        magnifyCardDialog = new Dialog(context);
        magnifyCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        magnifyCardDialog.setContentView(R.layout.pick_customdialog3);
        magnifyCardDialog.setTitle("My Custom Dialog");
        magnifyCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView cardImage = (ImageView) magnifyCardDialog.findViewById(R.id.pickCard_noDesc);
        LinearLayout card = (LinearLayout) magnifyCardDialog.findViewById(R.id.pickview_noDesc);

        if(placeURL == null) {
            Glide.with(context)
                    .load(Application.getInstance().getBaseImageUrl() + cardData.getCard_image_url())
                    .into(cardImage);
        } else {
            Glide.with(context).load(placeURL).into(cardImage);
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magnifyCardDialog.dismiss();
            }
        });
        magnifyCardDialog.show();
    }
}

