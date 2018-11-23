package com.example.tourproject.Map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.tourproject.R;
import com.example.tourproject.StoryList.RecyclerItemClickListener;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.StoryPlayActivity;
import com.example.tourproject.collect.MyJobService;

import java.io.File;
import java.util.ArrayList;

//activity_map
//layout : vertical_recycler_items
//src : VerticalnAdapter, VerticalData, HorizonViewHolder (http://diordna.tistory.com/19?category=677940), RecyclerItemClickListener
public class MapActivity extends AppCompatActivity implements ImageButton.OnClickListener{

    //StoryListActivity에서 받아온 story_id
    String id;
    String mid;

    SQLiteDatabase database = null;
    String dbName = "test1.db";

    ArrayList<String> arr_m2state_list = null;
    ArrayList<String> arr_m2id_list = null;
    RecyclerView mHorizonView;
    VerticalAdapter mAdapter;

    ImageButton btns[];
    FrameLayout layouts[];

    MyJobService b = new MyJobService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //db 생성 메소드
        createDatabase();

        Log.i("값 ", Double.toString(b.getMapx()));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);
        home.setBackgroundResource(R.drawable.logo);

        //StoryListActivity에서 보낸 story_id를 저장
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("p_id");
        mid = bundle.getString("m_id");

        btns = new ImageButton[3];
        layouts = new FrameLayout[3];
        btns[0] = (ImageButton) findViewById(R.id.imageView1);
        layouts[0] = (FrameLayout) findViewById(R.id.imageView1_1);
        btns[1] = (ImageButton) findViewById(R.id.imageView2);
        layouts[1] = (FrameLayout) findViewById(R.id.imageView2_2);
        btns[2] = (ImageButton) findViewById(R.id.imageView3);
        layouts[2] = (FrameLayout) findViewById(R.id.imageView3_3);
        btns[0].setBackgroundResource(R.drawable.a_3);
        btns[1].setBackgroundResource(R.drawable.a_3);
        btns[2].setBackgroundResource(R.drawable.a_3);
        btns[0].setOnClickListener(this);
        btns[1].setOnClickListener(this);
        btns[2].setOnClickListener(this);
        offbtn();
        onbtn();
        updatedb(b);

        selectDatas("select state, map2_id from Map2 where story_id="+id+" and map1_id="+mid);
        viewcheck();

        mHorizonView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mHorizonView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(arr_m2state_list.get(position).equals("1") || arr_m2state_list.get(position).equals("0")) {
                    Intent intent = new Intent(MapActivity.this, StoryPlayActivity.class);
                    //story_id를 넘겨준다
                    intent.putExtra("p_id", id);
                    intent.putExtra("m_id", mid);
                    intent.putExtra("m2_id", position + 1);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void viewcheck(){
        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        ArrayList<VerticalData> data = new ArrayList<>();
        for(int i = 0; i < arr_m2state_list.size(); i++)
            data.add(new VerticalData(R.drawable.a_4, mid + "-" + (i + 1), Integer.parseInt(arr_m2state_list.get(i)), Integer.parseInt(arr_m2id_list.get(i))));
        final LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);
        mLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mHorizonView.setLayoutManager(mLayoutManger);
        mAdapter = new VerticalAdapter();
        mAdapter.setData(data);
        mHorizonView.setAdapter(mAdapter);
    }

    public void onbtn(){
       btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.a_3);
       layouts[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.imagebutton_border);
    }

    public void offbtn(){
        btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.a_3);
        layouts[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.imagebutton_border2);
    }

    public void selectDatas(String sql){
        arr_m2state_list = new ArrayList<String>();
        arr_m2id_list = new ArrayList<String>();
        String r = "";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            arr_m2state_list.add(result.getString(0));
            Log.i("아아앙", result.getString(1));
            arr_m2id_list.add(result.getString(1));
            result.moveToNext();
        }
        result.close();
    }

    //db 생성 메소드
    public void createDatabase(){
        File file = new File(getFilesDir(), dbName);
        try{
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView1:
                offbtn();
                mid = "1";
                onbtn();
                updatedb(b);
                selectDatas("select state, map2_id from Map2 where story_id="+id+" and map1_id="+mid);
                viewcheck();
                break;
            case R.id.imageView2:
                offbtn();
                mid = "2";
                onbtn();
                updatedb(b);
                selectDatas("select state, map2_id from Map2 where story_id="+id+" and map1_id="+mid);
                viewcheck();
                break;
            case R.id.imageView3:
                offbtn();
                mid = "3";
                onbtn();
                updatedb(b);
                selectDatas("select state, map2_id from Map2 where story_id="+id+" and map1_id="+mid);
                viewcheck();
                break;
        }
    }

    public void updatedb(MyJobService b){
        if(LocatedPlace(b.getMapx(), b.getMapy())) {
            Log.i("여기여기","ㅇㅇㅇ");
            database.execSQL("update Map2 set state = 1 where map1_id=" + mid + " and story_id=" + id + " and map2_id=" + 2);
        }
    }
    public boolean LocatedPlace(double mapx, double mapy){
        Log.i("여기여기ㅇㅇ","ㅇㅇㅇ");
        double p_mapx, p_mapy;
        p_mapx = 126.977041;
        p_mapy = 37.579652;
        double diffmapy = LatitudeInDifference(300);
        double diffmapx = LongitudeInDifference(p_mapy, 300);
        Log.i("mapy",Double.toString(mapy));
        Log.i("diff",Double.toString((p_mapy - diffmapy)));
        Log.i("diff2",Double.toString((p_mapy + diffmapy)));
        if(mapy >= (p_mapy - diffmapy) && mapy <= (p_mapy + diffmapy))
            if(mapx >= (p_mapx - diffmapx) && mapx <= (p_mapx + diffmapx))
                return true;
        return false;
    }

    //반경 m이내의 위도차(degree)
    public double LatitudeInDifference(int diff){
        //지구반지름
        final int earth = 6371000;    //단위m

        return (diff*360.0) / (2*Math.PI*earth);
    }

    //반경 m이내의 경도차(degree)
    public double LongitudeInDifference(double _latitude, int diff){
        //지구반지름
        final int earth = 6371000;    //단위m

        double ddd = Math.cos(0);
        double ddf = Math.cos(Math.toRadians(_latitude));

        return (diff*360.0) / (2*Math.PI*earth*Math.cos(Math.toRadians(_latitude)));
    }

}
