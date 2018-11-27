package com.example.tourproject.Collect;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.tourproject.MainActivity;
import com.example.tourproject.R;

import static com.example.tourproject.Collect.MyJobService.data;
import static com.example.tourproject.Collect.MyJobService.data2;
import static com.example.tourproject.Collect.MyJobService.mapy;


public class PlaceMainActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    ListView listView;
    ListView listView2;
    static ListviewAdapter adapter;
    static ListviewAdapter adapter2;
    String key = "1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_main);
        MyJobService.bAppRunned = true;
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
        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listview300);
        listView2 = (ListView)findViewById(R.id.listview2000);
        if(data2.size() == 0 && mapy == 0){
            Log.i("data2가 없습니다.", ",,");
            data.clear();
            data2.clear();
            //setGps();
        }



        Log.i("data2 전달됐어요", String.valueOf(data2.size()));
        adapter=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter2=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data2);
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
                Intent intent = new Intent(getApplicationContext(), overView2000.class);
                //intent.putExtra("img", data.get(position).getIcon());
                intent.putExtra("title", data2.get(position).getName());
                intent.putExtra("content_id", data2.get(position).getContent_id());
                intent.putExtra("contentType_id", data2.get(position).getContentType_id());
                intent.putExtra("mapx", data2.get(position).getMapx());
                intent.putExtra("mapy", data2.get(position).getMapy());
                startActivity(intent);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PlaceMainActivity.this, MainActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.restart) {
            recreate();
        }
    }
}//MainActivity class..

