package com.example.tourproject.StoryList;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.tourproject.R;
import com.example.tourproject.Util.StoryListManager;

//activity_story_list
//layout : horizon_recycler_items
//src : HorizonAdapter, StoryData, HorizonViewHolder (http://diordna.tistory.com/19?category=677940), RecyclerItemClickListener
public class StoryListActivity extends AppCompatActivity {

    RecyclerView mHorizonView;
    HorizonAdapter horizonAdapter;
    LinearLayoutManager mLayoutManger;

    public final static String TAG = "StoryList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        init();
        setRecyclerView();
    }

    public void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);
    }

    public void setRecyclerView() {
        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        mLayoutManger = new LinearLayoutManager(StoryListActivity.this);
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHorizonView.setLayoutManager(mLayoutManger);
        mHorizonView.setHasFixedSize(true);

        if (StoryListManager.getInstance().getStoryList().size() != 0) {
            horizonAdapter = new HorizonAdapter(StoryListActivity.this, StoryListManager.getInstance().getStoryList());
            mHorizonView.setAdapter(horizonAdapter);
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            finish();
        }
    }

}
