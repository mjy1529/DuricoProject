package com.example.tourproject.StoryPlay;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.CardBox.CardData;
import com.example.tourproject.GachaActivity;
import com.example.tourproject.Map.MapActivity;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Network.Application;
import com.example.tourproject.Util.CardManager;
import com.example.tourproject.Util.StoryPlayManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class StoryPlayActivity extends AppCompatActivity {

    int i = 0; //current page
    int v_cnt; //view 개수

    int map2_id; //map2_id

    TextView tv; //내용을 보여주는 textView
    ImageView imageView; //스토리 사진을 보여주는 imageView
    ArrayList<StoryPlayData> storyPlayList;
    TextView content; //person_name을 보여주는 textView

    StoryPlayData storyPlayData;
    Dialog MyDialog;

    public static final String TAG = "StoryPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        Intent intent = getIntent();
        map2_id = intent.getIntExtra("map2_id", 0);

        Log.d(TAG, map2_id+"");

        init();
        getStoryPlayList(map2_id);
        setStoryPlay();
    }

    public void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar2, null);
        actionBar.setCustomView(mCustomView);

        tv = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        content = (TextView) findViewById(R.id.content);
    }

    public void getStoryPlayList(int map2_id) {
        StoryPlayManager storyPlayDataManager = StoryPlayManager.getInstance();
        ArrayList<StoryPlayData> allStoryPlayList = storyPlayDataManager.getStoryPlayList();

        storyPlayList = new ArrayList<>();

        for (int i = 0; i < allStoryPlayList.size(); i++) {
            if (allStoryPlayList.get(i).getMap2_id() == map2_id) {
                storyPlayList.add(allStoryPlayList.get(i));
            }
        }
    }

    public void setStoryPlay() {
        //첫번째 페이지 세팅
        v_cnt = storyPlayList.size();
        storyPlayData = storyPlayList.get(0);
        tv.setText(storyPlayData.getPlay_content());

        // ***** person_name 존재여부에 따른 이벤트 처리 ***** //
        if (storyPlayData.getPerson_name().equals("null")) { //person_name이 없을 경우
            content.setVisibility(View.INVISIBLE); //사라지기
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(storyPlayData.getPerson_name());
        }

        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + storyPlayData.getPlay_image_url())
                .into(imageView);

        //화면 터치 시 다음 내용으로 전환
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i < v_cnt) { // 현재 페이지가 전체 뷰보다 작을 때 다음 view 보여주기
                    storyPlayData = storyPlayList.get(i);
                    tv.setText(storyPlayData.getPlay_content());

                    if (storyPlayData.getPerson_name().equals("null")) { //person_name이 없을 경우
                        content.setVisibility(View.INVISIBLE); //사라지기
                    } else {
                        content.setVisibility(View.VISIBLE);
                        content.setText(storyPlayData.getPerson_name());
                    }

                    Glide.with(StoryPlayActivity.this)
                            .load(Application.getInstance().getBaseImageUrl() + storyPlayData.getPlay_image_url())
                            .into(imageView);

                } else { // 마지막 페이지일 때
                    //해당 이야기에 맞는 카드가 있는지 검색하기
                    CardData cardData = findCard();
                    if (cardData != null && cardData.getMap2_id() != 0) {
                        if(cardData.getCard_category().equals("people")) {
                            updateOpenPeopleCard(cardData);
                        } else if(cardData.getCard_category().equals("story")) {
                            updateOpenStoryCard(cardData);
                        }
                        MyCustomAlertDialog(cardData);

                    } else {
                        finish();
                    }
                }
            }
        });
    }

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StoryPlayActivity.this, MapActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            finish();
        }
    }

    public CardData findCard() {
        ArrayList<CardData> peopleCardList = CardManager.getInstance().getPeopleCardList();
        ArrayList<CardData> storyCardList = CardManager.getInstance().getStoryCardList();

        for (int i = 0; i < peopleCardList.size(); i++) {
            if (peopleCardList.get(i).getMap2_id() == map2_id) {
                return peopleCardList.get(i);
            }
        }

        for (int i = 0; i < storyCardList.size(); i++) {
            if (storyCardList.get(i).getMap2_id() == map2_id) {
                return storyCardList.get(i);
            }
        }
        return null;
    }

    public void MyCustomAlertDialog(CardData cardData) {
        MyDialog = new Dialog(StoryPlayActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView cardName = (TextView) MyDialog.findViewById(R.id.cardName);
        TextView cardcontent = (TextView) MyDialog.findViewById(R.id.cardContent);
        ImageView cardimage = (ImageView) MyDialog.findViewById(R.id.gacha_card);
        LinearLayout card = (LinearLayout) MyDialog.findViewById(R.id.pickview);

        cardName.setText(cardData.getCard_name());
        cardcontent.setText(cardData.getCard_description());
        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + cardData.getCard_image_url())
                .into(cardimage);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
                finish();
            }
        });
        MyDialog.show();
    }

    //뽑은 카드 상태(close -> open) 업데이트
    public void updateOpenPeopleCard(final CardData cardData) {
        NetworkService networkService = Application.getInstance().getNetworkService();
        String user_id = UserManager.getInstance().getUserId();
        Call<String> request = networkService.updatePeopleCardState(user_id, cardData.getCard_idx());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    UserManager.getInstance().getOpenPeopleCardList().add(cardData.getCard_idx());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateOpenStoryCard(final CardData cardData) {
        NetworkService networkService = Application.getInstance().getNetworkService();
        String user_id = UserManager.getInstance().getUserId();
        Call<String> request = networkService.updateStoryCardState(user_id, cardData.getCard_idx());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    UserManager.getInstance().getOpenStoryCardList().add(cardData.getCard_idx());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
