package com.example.tourproject;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.collect.Listviewitem;
import com.example.tourproject.collect.MyJobService;
import com.example.tourproject.collect.PlaceMainActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    ArrayList<Listviewitem> data = new ArrayList<>();

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), MyJobService.class);
        //액션바-------------------------------
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
        //여기까지------------------------------
        Button btn0 = (Button) findViewById(R.id.btnCard);
        btn0.setOnClickListener(this);
        Button btn1 = (Button) findViewById(R.id.btnCollect);
        Button btn2 = (Button) findViewById(R.id.btnStart);
        Button btn3 = (Button) findViewById(R.id.btnPick);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        JobInfo jobInfo = new JobInfo.Builder(0, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(1000 * 60 * 15)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);
    }
    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            //onBackPressed();
            recreate();
        }
    }
    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnCard:
                intent = new Intent(MainActivity.this, CardBoxActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCollect:
                intent = new Intent(MainActivity.this, PlaceMainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnStart:
                intent = new Intent(MainActivity.this, StoryListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnPick:
                NetworkInfo mNetworkState = getNetworkInfo();
                if(mNetworkState != null && mNetworkState.isConnected()){
                    if(mNetworkState.getType() == ConnectivityManager.TYPE_WIFI || mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE){
                        Log.i("인터넷 연결됨", "인터넷 연결됨");
                        intent = new Intent(MainActivity.this, GachaActivity.class);
                        startActivity(intent);
                    }
                }
                else {
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
}
