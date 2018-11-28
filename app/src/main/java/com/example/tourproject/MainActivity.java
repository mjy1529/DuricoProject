package com.example.tourproject;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.BackPressCloseHandler;

import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.Util.UserManager;
import com.example.tourproject.Collect.Listviewitem;
import com.example.tourproject.Collect.MyJobService;
import com.example.tourproject.Collect.PlaceMainActivity;

import java.util.ArrayList;

import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    ArrayList<Listviewitem> data = new ArrayList<>();
    private BackPressCloseHandler backPressCloseHandler;

    ImageButton btn0;
    public static Context mContext;

    public static final String TAG = "MainActivity";

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
*/
        mContext = this;

        MyJobService.bAppRunned = true;

        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), MyJobService.class);

        doActionbar();

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

        Button btn1 = (Button) findViewById(R.id.btnCollect);
        Button btn2 = (Button) findViewById(R.id.btnStart);
        Button btn3 = (Button) findViewById(R.id.btnPick);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
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
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

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

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            //onBackPressed();
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
            case R.id.btnCollect:
                AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity.this);
                Check.setTitle("사용자 위치 재탐색")
                        .setMessage("현재 위치를 탐색하시겠습니까?\n위치 탐색 후 관광지 내역을 재구성합니다.\n선택 후 잠시만 기다려주십시오 :-)")
                        .setPositiveButton("탐색하여 관광지 재구성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                                dialog.dismiss();
                                while(waiting()!=7);
                                Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("재탐색 없이 계속", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                                startActivity(intent);
                            }
                        });
                Check.setCancelable(false);
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
                break;
        }
    }

    // 뒤로가기 두번 누를 시 앱 종료
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
    public int waiting(){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 7;
    }
    public void changeProfileImage(String card_url) {
        Glide.with(MainActivity.this)
                .load(Application.getInstance().getBaseImageUrl() + card_url)
                .apply(new RequestOptions().centerCrop())
                .into(btn0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
