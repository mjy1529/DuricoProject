package com.example.tourproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tourproject.CardBox.CardBoxActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{
    private Button serviceStart, serviceEnd;
    ArrayList<Listviewitem> data = new ArrayList<>();
    double mapx;
    double mapy;
    PlaceMainActivity placeMainActivity = new PlaceMainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn0 = (Button) findViewById(R.id.btnCard);
        btn0.setOnClickListener(this);
        Button btn1 = (Button) findViewById(R.id.btnCollect);
        Button btn2 = (Button) findViewById(R.id.btnStart);
        Button btn3 = (Button) findViewById(R.id.btnPick);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        serviceStart = (Button)findViewById(R.id.service_start);
        serviceEnd = (Button)findViewById(R.id.service_end);
        serviceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });
        serviceEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
            }
        });
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
            case R.id.service_start:
                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
                break;
            case R.id.service_end:
                Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
                break;
        }
    }
}
