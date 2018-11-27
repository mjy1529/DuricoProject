package com.example.tourproject.StoryPlay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.Map.MapActivity;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.StoryPlayManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryPlayActivity extends AppCompatActivity {

    int i = 0; //current page
    int v_cnt; //view 개수

    TextView tv; //내용을 보여주는 textView
    ImageView imageView; //스토리 사진을 보여주는 imageView
    ArrayList<StoryPlayData> storyPlayList;

    public static final String TAG = "StoryPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        Intent intent = getIntent();
        int map2_id = intent.getIntExtra("map2_id", 0);

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
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar2, null);
        actionBar.setCustomView(mCustomView);

        tv = (TextView) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.imageView);
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
        v_cnt = storyPlayList.size();
        tv.setText(storyPlayList.get(0).getPlay_content());
        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + storyPlayList.get(0).getPlay_image_url())
                .into(imageView);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i < v_cnt) { // 현재 페이지가 전체 뷰보다 작을 때 다음 view 보여주기
                    tv.setText(storyPlayList.get(i).getPlay_content());
                    Glide.with(StoryPlayActivity.this)
                            .load(Application.getInstance().getBaseImageUrl() + storyPlayList.get(i).getPlay_image_url())
                            .into(imageView);
                } else { // 마지막 페이지일 때
                    finish();
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
}
