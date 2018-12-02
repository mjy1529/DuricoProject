package com.durico.tourproject.Collect;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.durico.tourproject.CardBox.CardBoxActivity;
import com.durico.tourproject.GachaActivity;
import com.durico.tourproject.MainActivity;
import com.durico.tourproject.R;
import com.durico.tourproject.SplashActivity;
import com.durico.tourproject.Util.UserManager;

import static com.durico.tourproject.Collect.MyJobService.data;
import static com.durico.tourproject.Collect.MyJobService.data2;
import static com.durico.tourproject.Collect.MyJobService.mapx;
import static com.durico.tourproject.Collect.MyJobService.mapy;


public class PlaceMainActivity extends AppCompatActivity implements AdapterView.OnClickListener {

    ListView listView;
    ListView listView2;
    static com.durico.tourproject.Collect.ListviewAdapter adapter;
    static com.durico.tourproject.Collect.ListviewAdapter adapter2;
    String key = "1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_main);
        MyJobService.bAppRunned = true;

        if (MainActivity.progressDialog != null)
            MainActivity.progressDialog.dismiss();
        doActionbar();

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listview300);
        listView2 = (ListView) findViewById(R.id.listview2000);
        if (data2.size() == 0 && mapy != 0) {
            //Log.i("data2가 없습니다.", ",,");
            AlertDialog.Builder Check = new AlertDialog.Builder(PlaceMainActivity.this);
            Check.setTitle("탐색 불가")
                    .setMessage("검색 가능한 관광지가 없습니다.");
            Check.setCancelable(true);
            Check.show();
            data.clear();
            data2.clear();
            //setGps();
        }
        if(mapx == 0 && mapy == 0){
            //Log.i("data2가 없습니다.", ",,");
            AlertDialog.Builder Check = new AlertDialog.Builder(PlaceMainActivity.this);
            Check.setTitle("탐색 불가")
                    .setMessage("위치 탐색에 실패하였습니다.\n위치 권한이 허용되어있는지 확인해주세요.");
            Check.setCancelable(true);
            Check.show();
            data.clear();
            data2.clear();
            //setGps();
        }

        Log.i("data2 전달됐어요", String.valueOf(data2.size()));
        adapter = new com.durico.tourproject.Collect.ListviewAdapter(PlaceMainActivity.this, R.layout.item, data);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter2 = new com.durico.tourproject.Collect.ListviewAdapter(PlaceMainActivity.this, R.layout.item, data2);
        adapter2.notifyDataSetChanged();
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), overView.class);
                //intent.putExtra("img", data.get(position).getIcon());
                intent.putExtra("title", data.get(position).getName());
                intent.putExtra("content_id", data.get(position).getContent_id());
                intent.putExtra("contentType_id", data.get(position).getContentType_id());
                intent.putExtra("mapx", data.get(position).getMapx());
                intent.putExtra("mapy", data.get(position).getMapy());
                startActivity(intent);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), com.durico.tourproject.Collect.overView2000.class);
                //intent.putExtra("img", data.get(position).getIcon());
                intent.putExtra("title", data2.get(position).getName());
                intent.putExtra("content_id", data2.get(position).getContent_id());
                intent.putExtra("contentType_id", data2.get(position).getContentType_id());
                intent.putExtra("mapx", data2.get(position).getMapx());
                intent.putExtra("mapy", data2.get(position).getMapy());
                startActivity(intent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        //네트워크 연결됨
                    }

                    @Override
                    public void onLost(Network network) {
                        //네트워크 끊어짐
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(PlaceMainActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.finishAffinity(PlaceMainActivity.this);
                            }
                        });
                        try {
                            alert.show();
                        } catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }
                    }
                });
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            /*Intent intent = new Intent(PlaceMainActivity.this, MainActivity.class);
            startActivity(intent);*/
            onBackPressed();
        }
    }

    public void doActionbar() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PlaceMainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.restart) {
            recreate();
        }
        switch (v.getId()) {
            case R.id.pcardCnt:
            case R.id.pecardCnt:
            case R.id.scardCnt:
                Intent intent = new Intent(PlaceMainActivity.this, CardBoxActivity.class);
                startActivity(intent);
                break;
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
}//MainActivity class..

