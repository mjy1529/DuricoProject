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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    //StoryListActivity에서 받아온 story_id
    String id;
    String mid;

    SQLiteDatabase database = null;
    String dbName = "test1.db";

    ArrayList<String> arr_mstate_list = null;

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
        String storyTitle = bundle.getString("p_title");
        TextView tv = (TextView) findViewById(R.id.storyTitle);
        tv.setText(storyTitle);

        ImageView iv[] = new ImageView[3];
        iv[0] = (ImageView) findViewById(R.id.imageView1);
        iv[1] = (ImageView) findViewById(R.id.imageView2);
        iv[2] = (ImageView) findViewById(R.id.imageView3);

        arr_mstate_list = new ArrayList<String>();
        selectData(Integer.parseInt(id));
        for(int i = 0; i < arr_mstate_list.size(); i++) {
            Log.i("ddddwk", arr_mstate_list.get(i));
            if (arr_mstate_list.get(i).equals("1"))
                iv[i].setImageResource(R.drawable.img2);
            else
                iv[i].setImageResource(R.drawable.img1);
        }
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
}
