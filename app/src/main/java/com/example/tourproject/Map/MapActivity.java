package com.example.tourproject.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.MainActivity;
import com.example.tourproject.SplashActivity;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.MapManager;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Collect.MyJobService;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements ImageButton.OnClickListener {
    String mid;

    RecyclerView mHorizonView;
    public static VerticalAdapter mAdapter;

    ImageView imageViews[];
    FrameLayout layouts[];
    TextView textView2;

    MyJobService b = new MyJobService();

    MapManager mapDataManager;
    NetworkService networkService;
    ArrayList<Map1Data> selectedStoryMap;

    int p;
    Map1Data map1Data;

    public final static String TAG = "MAP Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();

        Intent intent = getIntent();
        String story_id = intent.getStringExtra("story_id");
        setMap(story_id);
        map1Data = selectedStoryMap.get(0);
        updateMap2(b);

        ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback()
                {
                    @Override
                    public void onAvailable( Network network )
                    {
                        //네트워크 연결됨
                    }

                    @Override
                    public void onLost( Network network )
                    {
                        //네트워크 끊어짐
                        final AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                Intent i = getBaseContext().getPackageManager().
                                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                dialog.dismiss();
                                finish();
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);*/
                                ActivityCompat.finishAffinity(MapActivity.this);
                            }
                        });
                        alert.show();
                    }
                } );
    }

    public void onBtn(int position) {
        layouts[position].setBackgroundResource(R.drawable.imagebutton_border);
    }

    public void offBtn(int position) {
        layouts[position].setBackgroundResource(R.drawable.imagebutton_border2);
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        offBtn(p);
        switch (v.getId()) {
            case R.id.imageView1:
                position = 0;
                break;
            case R.id.imageView2:
                position = 1;
                break;
            case R.id.imageView3:
                position = 2;
                break;
            case R.id.pecardCnt:
            case R.id.pcardCnt:
            case R.id.scardCnt:
                Intent intent = new Intent(MapActivity.this, CardBoxActivity.class);
                startActivity(intent);
                return;
        }
        p = position;
        setMap2(position);
        map1Data = selectedStoryMap.get(position);
        updateMap2(b);
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
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
        home.setBackgroundResource(R.drawable.logo);

        mapDataManager = MapManager.getInstance();
        networkService = Application.getInstance().getNetworkService();
        selectedStoryMap = new ArrayList<>();

        imageViews = new ImageButton[3];
        imageViews[0] = (ImageButton) findViewById(R.id.imageView1);
        imageViews[1] = (ImageButton) findViewById(R.id.imageView2);
        imageViews[2] = (ImageButton) findViewById(R.id.imageView3);
        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);
        imageViews[2].setOnClickListener(this);

        layouts = new FrameLayout[3];
        layouts[0] = (FrameLayout) findViewById(R.id.imageView1_1);
        layouts[1] = (FrameLayout) findViewById(R.id.imageView2_2);
        layouts[2] = (FrameLayout) findViewById(R.id.imageView3_3);

        textView2 = (TextView) findViewById(R.id.textView2);

        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        mHorizonView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setMap(String story_id) {
        if (mapDataManager.getMapList().size() != 0) {
            for (int i = 0; i < mapDataManager.getMapList().size(); i++) {
                if (mapDataManager.getMapList().get(i).story_id.equals(story_id)) {
                    selectedStoryMap.add(mapDataManager.getMapList().get(i));
                }
            }

            for (int i = 0; i < selectedStoryMap.size(); i++) {
                Glide.with(this)
                        .load(Application.getInstance().getBaseImageUrl() + selectedStoryMap.get(i).map1_image_url)
                        .into(imageViews[i]);
                imageViews[i].setBackgroundDrawable(Drawable.createFromPath(selectedStoryMap.get(i).map1_image_url));
                Log.d(TAG, Application.getInstance().getBaseImageUrl() + selectedStoryMap.get(i).map1_image_url);
            }
            setMap2(0);
            p = 0;
        } else {

        }
    }

    public void setMap2(int position) {
        if (selectedStoryMap != null && selectedStoryMap.size() != 0) {
            onBtn(position);
            textView2.setText(selectedStoryMap.get(position).map1_title);
                mAdapter = new VerticalAdapter(this, selectedStoryMap.get(position).map2List);
                mHorizonView.setAdapter(mAdapter);
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
//            onBackPressed();
            finish();
        }
    }

    public void updateMap2(MyJobService b) {
        Log.d("MAP JOBSERVICE", b.getMapx() +", " + b.getMapy());
        if (LocatedPlace(b.getMapx(), b.getMapy())) {
            // ***** db에 map2의 상태 1로 update 하기 ****** //
            int map2_id = 0;
            for(int i=0; i<map1Data.getMap2List().size(); i++) {
                if(map1Data.getMap2List().get(i).getMap1_id() == Integer.parseInt(map1Data.getMap_id())
                        && map1Data.getMap2List().get(i).getMap2_position() == 2) {
                    map2_id = map1Data.getMap2List().get(i).getMap2_id();

                    UserManager.getInstance().getMap2StateList().get(map2_id).setMap2_state(1);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
            Call<String> request = networkService.updateMap2State(UserManager.getInstance().getUserId(), map2_id);
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "MAP2 두번째 이야기 활성화");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "MAP2 두번째 이야기 활성화 실패");
                }
            });
            // ****************************************** //
        }
    }

    public boolean LocatedPlace(double mapx, double mapy) {
        double p_mapx, p_mapy;

        // ************ 추가 ************ //
//        p_mapx = 126.977041;
//        p_mapy = 37.579652;
        p_mapx = map1Data.getMap1_mapx();
        p_mapy = map1Data.getMap1_mapy();
        // ***************************** //
        Log.i(TAG, "p_mapx : " + p_mapx + ", p_mapy : " + p_mapy);

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