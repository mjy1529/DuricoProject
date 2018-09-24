package com.example.tourproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

public class DetailStoryActivity extends AppCompatActivity {

    //db를 이용하기 위한 변수
    SQLiteDatabase database = null;
    String dbName = "test1.db";
    //StoryListActivity에서 받아온 story_id
    String id;
    //해당 큰스토리 안에 작은 스토리를 읽었는지? 안읽었는지? 판단하는 state 변수
    ArrayList<String> arrlist = null;
    int i;
    //해당 큰스토리 안에 작은 스토리가 몇개인지를 저장하는 변수
    int d_cnt;

    //뒤로가기 버튼을 위한 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home :
                Intent intent = new Intent(DetailStoryActivity.this, StoryListActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_story);

        //db 생성 메소드
        createDatabase();

        //StoryListActivity에서 보낸 story_id를 저장
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("p_id");
        //선택한 스토리에 해당하는 작은 스토리들이 몇개인지 세면서 작은 스토리에 현재 상태값을 저장한다.
        arrlist = new ArrayList<String>();
        d_cnt = checkcount();

        LinearLayout l1 = (LinearLayout) findViewById(R.id.linearLayout_detailstory);
        for(i = 0; i < d_cnt; i++){
            //선택한 스토리에 해당하는 작은 스토리들만큼 버튼을 동적으로 생성
            final ImageButton btn = new ImageButton(this);
            btn.setId(i + 1);//버튼에 id는 detailStory_id와 동일하게 함

            Bitmap bitmap = null;
            //현재 스토리 상태에 따라 다른 버튼 이미지를 보여주기 위해
            switch (Integer.parseInt(arrlist.get(i))) {
                case 1:
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.yellow)).getBitmap();
                    break;
                case 2:
                case 3:
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.g_circle)).getBitmap();
                    break;
                case 0:
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.c_yellow)).getBitmap();
                    break;
            }
            //버튼 이미지가 커서 작게 보여주게 하기 위해
            bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
            btn.setImageBitmap(bitmap);
            btn.setBackgroundColor((Color.parseColor("#00ff0000")));
            l1.addView(btn);
            btn.setOnClickListener(new ImageButton.OnClickListener(){
                public void onClick(View v){
                    //선택한 detailStory_id를 저장하는 변수
                    int tmp = btn.getId();
                    //선택한 세부 스토리의 상태가 1(오픈가능) 상태이거나 0(읽음) 상태일때만 ViewsActivity로 이동
                    if(arrlist.get(tmp - 1).equals("1") || arrlist.get(tmp - 1).equals("0")){
                        Intent intent = new Intent(DetailStoryActivity.this, ViewsActivity.class);
                        intent.putExtra("s_id", id);
                        intent.putExtra("d_id", Integer.toString(btn.getId()));
                        intent.putExtra("d_cnt", d_cnt);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    //selec을 이용해 DetailStorys table에서 state를 얻어오는 메소드 겸
    // 선택한 story_id에 해당하는 DetailStorys에 레코드가 몇개인지 반환하는 메소드
    public int checkcount(){
        int i = 0;
        String sql = "select state from DetailStorys where story_id="+id;
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            arrlist.add(result.getString(0));
            result.moveToNext();
            i++;
        }
        result.close();
        return i;
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
