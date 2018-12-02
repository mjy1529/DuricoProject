package com.example.tourproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.tourproject.Map.Map1Result;
import com.example.tourproject.Map.Map2Data;
import com.example.tourproject.Map.Map2Result;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.BackPressCloseHandler;

import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.Util.MapManager;
import com.example.tourproject.Util.UserManager;
import com.example.tourproject.Collect.Listviewitem;
import com.example.tourproject.Collect.MyJobService;
import com.example.tourproject.Collect.PlaceMainActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener, Button.OnTouchListener {

    ArrayList<Listviewitem> data = new ArrayList<>();
    private BackPressCloseHandler backPressCloseHandler;

    ImageButton btn0;
    ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;

    int s = 0;

    public static Context mContext;

    public static final String TAG = "MainActivity";

    public static ProgressDialog progressDialog;
    NetworkService networkService;
    MapManager mapManager;

    @SuppressLint("ClickableViewAccessibility")
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedPermission.with(MainActivity.this)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("수집 기능을 위해서는 위치 권한이 필요합니다.")
            .setDeniedMessage("위치 수집을 원하신다면\n[설정] > [권한] 에서 위치 권한을 허용해 주십시오.\n")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE)
            .check();

        mContext = this;

        MyJobService.bAppRunned = true;

        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), MyJobService.class);

        doActionbar();

        networkService = Application.getInstance().getNetworkService();
        mapManager = MapManager.getInstance();
        setMapDataManager();

        btn0 = (ImageButton) findViewById(R.id.btnCard);

        btn0.setOnClickListener(this);
        if(UserManager.getInstance().getUser_card_url() != null) { //사용자가 메인이미지를 선택하였을 경우
            changeProfileImage(UserManager.getInstance().getUser_card_url());
            btn0.setBackground(new ShapeDrawable(new OvalShape()));
            btn0.setClipToOutline(true);
        } else { //처음 실행되거나 사용자가 메인이미지를 선택하지 않았을 경우
            btn0.setBackground(getResources().getDrawable(R.drawable.main));
            btn0.setBackground(new ShapeDrawable(new OvalShape()));
            btn0.setClipToOutline(true);
        }

        btn0.setBackground(new ShapeDrawable(new OvalShape()));
        btn0.setClipToOutline(true);
        btn0.setOnClickListener(this);

        btn1 = (ImageButton) findViewById(R.id.btnCollect);
        btn2 = (ImageButton) findViewById(R.id.btnStart);
        btn3 = (ImageButton) findViewById(R.id.btnPick);
        btn1.setOnTouchListener(this);
        btn2.setOnTouchListener(this);
        btn3.setOnTouchListener(this);

        JobInfo jobInfo = new JobInfo.Builder(0, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(1000 * 60 * 180)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);

        String user_card_url = UserManager.getInstance().getUser_card_url();
        if (user_card_url != null && !user_card_url.equals("null")) { //사용자가 메인이미지를 선택하였을 경우
            changeProfileImage(UserManager.getInstance().getUser_card_url());
        } else { //처음 실행되거나 사용자가 메인이미지를 선택하지 않았을 경우
            btn0.setBackground(getResources().getDrawable(R.drawable.main));
        }

        backPressCloseHandler = new BackPressCloseHandler(this);

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
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        try {
                            alert.show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }
                    }
                } );
    }

    public boolean onTouch(View v, MotionEvent event){
        ImageButton i = null;
        int tmp = -1;
        switch (v.getId()) {
            case R.id.btnCollect:
                i = btn1;
                tmp = 0;
                break;
            case R.id.btnStart:
                i = btn2;
                tmp = 1;
                break;
            case R.id.btnPick:
                i = btn3;
                tmp = 2;
                break;
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                i.getBackground().setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                i.getBackground().clearColorFilter();
                if(tmp == 0) {
                    doCollect();
                }else if(tmp == 1){
                    Intent intent = new Intent(MainActivity.this, StoryListActivity.class);
                    startActivity(intent);
                }else if(tmp == 2){
                    doGacha();
                }
                break;
        }
        return true;


    }
    public void doGacha(){
        NetworkInfo mNetworkState = getNetworkInfo();
        if (mNetworkState != null && mNetworkState.isConnected()) {
            if (mNetworkState.getType() == ConnectivityManager.TYPE_WIFI || mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.i("인터넷 연결됨", "인터넷 연결됨");
                Intent intent = new Intent(MainActivity.this, GachaActivity.class);
                startActivity(intent);
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("네트워크");
            alert.setMessage("네트워크가 연결되지 않았습니다.");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
    }
    public void doCollect(){
        s = 0;
        AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity.this);
        Check.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(s == 1) {
                    progressDialog = ProgressDialog.show(MainActivity.this, "잠시 기다려주세요", "위치를 찾고있습니다.", true);
                    progressDialog.onStart();
                }
            }
        });
        Check.setTitle("사용자 위치 재탐색")
                .setMessage("현재 위치를 탐색하시겠습니까?\n위치 탐색 후 관광지 내역을 재구성합니다.\n선택 후 잠시만 기다려주십시오 :-)")
                .setPositiveButton("탐색하여 관광지 재구성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s = 1;
                        recreate();
                        dialog.dismiss();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                startActivity(intent);
                            }
                        }, 8000);// 0.5초 정도 딜레이를 준 후 시작


                    }
                })
                .setNegativeButton("재탐색 없이 계속", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s = 1;
                        dialogInterface.dismiss();
                        if(TedPermission.isGranted(MainActivity.this, "Manifest.permission.ACCESS_FINE_LOCATION", "Manifest.permission.ACCESS_COARSE_LOCATION"))
                        {
                            while (!MyJobService.finished) ;
                        }
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                startActivity(intent);
                            }
                        }, 2000);// 0.5초 정도 딜레이를 준 후 시작
                    }
                });
        Check.setCancelable(true);
        Check.show();
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
        home.setVisibility(View.INVISIBLE);
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            recreate();
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnCard:
                intent = new Intent(MainActivity.this, CardBoxActivity.class);
                startActivity(intent);
                break;
            case R.id.pcardCnt:
            case R.id.pecardCnt:
            case R.id.scardCnt:
                intent = new Intent(MainActivity.this, CardBoxActivity.class);
                startActivity(intent);
                break;

            /*case R.id.btnCollect:
                AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity.this);
                Check.setTitle("사용자 위치 재탐색")
                        .setMessage("현재 위치를 탐색하시겠습니까?\n위치 탐색 후 관광지 내역을 재구성합니다.\n선택 후 잠시만 기다려주십시오 :-)")
                        .setPositiveButton("탐색하여 관광지 재구성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                                dialog.dismiss();
                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 6000);// 0.5초 정도 딜레이를 준 후 시작


                            }
                        })
                        .setNegativeButton("재탐색 없이 계속", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                startActivity(intent);
                            }
                        });
                Check.setCancelable(true);
                Check.show();
                break;
            case R.id.btnStart:
                intent = new Intent(MainActivity.this, StoryListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnPick:
                NetworkInfo mNetworkState = getNetworkInfo();
                if (mNetworkState != null && mNetworkState.isConnected()) {
                    if (mNetworkState.getType() == ConnectivityManager.TYPE_WIFI || mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.i("인터넷 연결됨", "인터넷 연결됨");
                        intent = new Intent(MainActivity.this, GachaActivity.class);
                        startActivity(intent);
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("네트워크");
                    alert.setMessage("네트워크가 연결되지 않았습니다.");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
                break;*/
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "권한 거부", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    // 뒤로가기 두번 누를 시 앱 종료
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
    public void changeProfileImage(String card_url) {
        Glide.with(MainActivity.this)
                .load(Application.getInstance().getBaseImageUrl() + card_url)
                .apply(new RequestOptions().centerCrop())
                .into(btn0);
        btn0.setBackground(new ShapeDrawable(new OvalShape()));
        btn0.setClipToOutline(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("아아아", Integer.toString(SplashActivity.state));
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
    }

    public void setMapDataManager() {
        Call<Map1Result> request = networkService.getAllMap1List();
        request.enqueue(new Callback<Map1Result>() {
            @Override
            public void onResponse(Call<Map1Result> call, Response<Map1Result> response) {
                if (response.isSuccessful()) {
                    Map1Result map1Result = response.body();
                    mapManager.setMapList(map1Result.map1);
                    for (int i = 0; i < mapManager.getMapList().size(); i++) {
                        getMap2(mapManager.getMapList().get(i).getMap_id()); //map_id로 map2 검색
                    }
                    Log.d(TAG, "MAP 받아오기 성공");
                }
            }

            @Override
            public void onFailure(Call<Map1Result> call, Throwable t) {
                Log.d(TAG, "MAP 받아오기 실패");
            }
        });
    }

    public void getMap2(final String map1_id) {
        Call<Map2Result> request = networkService.getMap2List(map1_id);
        request.enqueue(new Callback<Map2Result>() {
            @Override
            public void onResponse(Call<Map2Result> call, Response<Map2Result> response) {
                if (response.isSuccessful()) {
                    Map2Result map2Result = response.body();
                    ArrayList<Map2Data> map2List = map2Result.map2;

                    mapManager.getMapList().get(Integer.parseInt(map1_id)).setMap2List(map2List);
                }
            }

            @Override
            public void onFailure(Call<Map2Result> call, Throwable t) {
            }
        });
    }
}