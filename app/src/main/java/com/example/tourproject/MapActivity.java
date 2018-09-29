package com.example.tourproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

import java.io.File;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements ImageButton.OnClickListener{

    //StoryListActivity에서 받아온 story_id
    String id;
    String mid;

    SQLiteDatabase database = null;
    String dbName = "test1.db";

    ArrayList<String> arr_mstate_list = null;
    RecyclerView mHorizonView;
    VerticalAdapter mAdapter;

    ImageButton btns[];

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MapActivity.this, StoryListActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //db 생성 메소드
        createDatabase();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.list)).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 90, true);
        actionBar.setHomeAsUpIndicator(new BitmapDrawable(bitmap));

        //StoryListActivity에서 보낸 story_id를 저장
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("p_id");
        mid = bundle.getString("m_id");

        btns = new ImageButton[3];
        btns[0] = (ImageButton) findViewById(R.id.imageView1);
        btns[1] = (ImageButton) findViewById(R.id.imageView2);
        btns[2] = (ImageButton) findViewById(R.id.imageView3);

        btns[0].setBackgroundResource(R.drawable.img1);
        btns[1].setBackgroundResource(R.drawable.img1);
        btns[2].setBackgroundResource(R.drawable.img1);
        btns[0].setOnClickListener(this);
        btns[1].setOnClickListener(this);
        btns[2].setOnClickListener(this);
        onbtn();
        viewcheck();
    }

    public void viewcheck(){
        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        ArrayList<HorizonData> data = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            data.add(new HorizonData(R.drawable.gyeongbokpalace,mid+"-"+(i+1)));
        final LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);
        mLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mHorizonView.setLayoutManager(mLayoutManger);
        mAdapter = new VerticalAdapter();
        mAdapter.setData(data);
        mHorizonView.setAdapter(mAdapter);
    }

    public void onbtn(){
       btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.img2);
    }

    public void offbtn(){
        btns[Integer.parseInt(mid) - 1].setBackgroundResource(R.drawable.img1);
    }

    public void selectData(int id){
        String sql = "select * from Map1 where story_id="+id;
        Log.i("dddd", sql);
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            arr_mstate_list.add(result.getString(2));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView1:
                offbtn();
                mid = "1";
                onbtn();
                viewcheck();
                break;
            case R.id.imageView2:
                offbtn();
                mid = "2";
                onbtn();
                viewcheck();
                break;
            case R.id.imageView3:
                offbtn();
                mid = "3";
                onbtn();
                viewcheck();
                break;
        }
    }
}
