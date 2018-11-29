package com.example.tourproject.StoryList;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourproject.R;
import com.example.tourproject.Util.StoryListManager;
import com.example.tourproject.Util.UserManager;

import retrofit2.http.HEAD;


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

        doActionbar();
        setRecyclerView();
    }

    public void doActionbar(){
        //액션바-------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
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

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
    }
}
