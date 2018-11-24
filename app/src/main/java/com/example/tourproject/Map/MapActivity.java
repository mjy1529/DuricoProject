package com.example.tourproject.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.MapDataManager;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.collect.MyJobService;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity implements ImageButton.OnClickListener {

    //StoryListActivity에서 받아온 mid
    String mid;

//    ArrayList<String> arr_m2state_list = null;
    RecyclerView mHorizonView;
    VerticalAdapter mAdapter;

    ImageView imageViews[];
    TextView textView2;

    MyJobService b = new MyJobService();

    MapDataManager mapDataManager;
    NetworkService networkService;
    ArrayList<Map1Data> selectedStoryMap;
    public final static String TAG = "MAP Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();

//        Log.i("값 ", Double.toString(b.getMapx()));

//        Bundle bundle = getIntent().getExtras();
//        mid = bundle.getString("m_id");

//        offbtn();
//        onbtn();
//        updatedb(b);
//
//        selectDatas("select state from Map2 where story_id="+id+" and map1_id="+mid);
//        viewcheck();
//
//        mHorizonView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mHorizonView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if(arr_m2state_list.get(position).equals("1") || arr_m2state_list.get(position).equals("0")) {
//                    Intent intent = new Intent(MapActivity.this, StoryPlayActivity.class);
//                    //story_id를 넘겨준다
//                    intent.putExtra("p_id", id);
//                    intent.putExtra("m_id", mid);
//                    intent.putExtra("m2_id", position + 1);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
    }

//    public void viewcheck() {
//        for (int i = 0; i < arr_m2state_list.size(); i++)
//            data.add(new VerticalData(R.drawable.gyeongbokpalace, mid + "-" + (i + 1), Integer.parseInt(arr_m2state_list.get(i))));
//    }

    public void onbtn() {
        //btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.img2);
    }

    public void offbtn() {
        //btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.img1);
    }

//    public void selectDatas(String sql) {
//        arr_m2state_list = new ArrayList<String>();
//        String r = "";
//        Cursor result = database.rawQuery(sql, null);
//        result.moveToFirst();
//        while (!result.isAfterLast()) {
//            arr_m2state_list.add(result.getString(0));
//            result.moveToNext();
//        }
//        result.close();
//    }

    @Override
    public void onClick(View v) {
        int position = 0;

        switch (v.getId()) {
            case R.id.imageView1:
                position = 0;
                //offbtn();
                //mid = "1";
                //onbtn();
                //updatedb(b);
                //selectDatas("select state from Map2 where story_id=" + id + " and map1_id=" + mid);
                //viewcheck();
                break;
            case R.id.imageView2:
                position = 1;
//                offbtn();
//                mid = "2";
//                onbtn();
//                updatedb(b);
                //selectDatas("select state from Map2 where story_id=" + id + " and map1_id=" + mid);
                //viewcheck();
                break;
            case R.id.imageView3:
                position = 2;
//                offbtn();
//                mid = "3";
//                onbtn();
//                updatedb(b);
                //selectDatas("select state from Map2 where story_id=" + id + " and map1_id=" + mid);
                //viewcheck();
                break;
        }

        setMap2(position);
    }

    public void updatedb(MyJobService b) {
        if (LocatedPlace(b.getMapx(), b.getMapy())) {
            Log.i("여기여기", "ㅇㅇㅇ");
            //database.execSQL("update Map2 set state = 1 where map1_id=" + mid + " and story_id=" + id + " and map2_id=" + 2);
        }
    }

    public boolean LocatedPlace(double mapx, double mapy) {
        Log.i("여기여기ㅇㅇ", "ㅇㅇㅇ");
        double p_mapx, p_mapy;
        p_mapx = 126.977041;
        p_mapy = 37.579652;
        double diffmapy = LatitudeInDifference(300);
        double diffmapx = LongitudeInDifference(p_mapy, 300);
        Log.i("mapy", Double.toString(mapy));
        Log.i("diff", Double.toString((p_mapy - diffmapy)));
        Log.i("diff2", Double.toString((p_mapy + diffmapy)));
        if (mapy >= (p_mapy - diffmapy) && mapy <= (p_mapy + diffmapy))
            if (mapx >= (p_mapx - diffmapx) && mapx <= (p_mapx + diffmapx))
                return true;
        return false;
    }

    //반경 m이내의 위도차(degree)
    public double LatitudeInDifference(int diff) {
        //지구반지름
        final int earth = 6371000;    //단위m

        return (diff * 360.0) / (2 * Math.PI * earth);
    }

    //반경 m이내의 경도차(degree)
    public double LongitudeInDifference(double _latitude, int diff) {
        //지구반지름
        final int earth = 6371000;    //단위m

        double ddd = Math.cos(0);
        double ddf = Math.cos(Math.toRadians(_latitude));

        return (diff * 360.0) / (2 * Math.PI * earth * Math.cos(Math.toRadians(_latitude)));
    }

    public void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.list)).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        actionBar.setHomeAsUpIndicator(new BitmapDrawable(bitmap));

        mapDataManager = MapDataManager.getInstance();
        networkService = Application.getInstance().getNetworkService();
        selectedStoryMap = new ArrayList<>();

        imageViews = new ImageView[3];
        imageViews[0] = (ImageView) findViewById(R.id.imageView1);
        imageViews[1] = (ImageView) findViewById(R.id.imageView2);
        imageViews[2] = (ImageView) findViewById(R.id.imageView3);

        textView2 = (TextView) findViewById(R.id.textView2);

        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        mHorizonView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String story_id = intent.getStringExtra("story_id");
        setMap(story_id);
    }

    public void setMap(String story_id) {
        for (int i = 0; i < mapDataManager.getMapList().size(); i++) {
            if (mapDataManager.getMapList().get(i).story_id.equals(story_id)) {
                selectedStoryMap.add(mapDataManager.getMapList().get(i));
            }
        }

        for (int i = 0; i < selectedStoryMap.size(); i++) {
            Glide.with(this)
                    .load(Application.getInstance().getBaseImageUrl() + selectedStoryMap.get(i).map1_image_url)
                    .into(imageViews[i]);
            Log.d(TAG, Application.getInstance().getBaseImageUrl() + selectedStoryMap.get(i).map1_image_url);
        }
        textView2.setText(selectedStoryMap.get(0).map1_title);
        setMap2(0);
    }

    public void setMap2(int position) {
        mAdapter = new VerticalAdapter(this, selectedStoryMap.get(position).map2List);
        mHorizonView.setAdapter(mAdapter);
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
}
