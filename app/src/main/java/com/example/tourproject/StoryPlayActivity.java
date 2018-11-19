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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tourproject.Map.MapActivity;

import java.io.File;

public class StoryPlayActivity extends AppCompatActivity {
    //DetailStoryActivity에서 받아온 story_id와 detailStory_id
    String s_id;
    String m_id;
    int m2_id;
    //db를 이용하기 위한 변수
    SQLiteDatabase database = null;
    String dbName = "test1.db";
    int i;
    //내용을 보여주는 textview
    TextView tv;
    //view 개수
    int v_cnt;
    //세부 스토리 개수
    int d_cnt;

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StoryPlayActivity.this, MapActivity.class);
                intent.putExtra("p_id", s_id);
                intent.putExtra("m_id", m_id);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        //db 생성 메소드
        createDatabase();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.list)).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        actionBar.setHomeAsUpIndicator(new BitmapDrawable(bitmap));

        //DetailStoryActivity에서 보낸 story_id와 detailStory_id와 세부스토리 개수를 저장
        Bundle bundle = getIntent().getExtras();
        s_id = bundle.getString("p_id");
        m_id = bundle.getString("m_id");
        m2_id = bundle.getInt("m2_id");

        //선택한 작은스토리에 해당하는 view들이 몇개인지 세서 저장한다
        v_cnt = Integer.parseInt(find("select count(*) as cnt from Views where story_id="+s_id+" and map1_id="+m_id+" and map2_id="+m2_id));
        i = 1;

        //textview에는 첫번째 view내용이 들어가 있다
        tv = (TextView) findViewById(R.id.content);
        tv.setText(find("select content from Views where story_id="+s_id+" and map1_id="+m_id+" and map2_id="+m2_id+" and view_id="+i));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                //마지막 view에 도달
                if(i > v_cnt){
                    //현재 세부스토리 상태가 읽기가능 1이면 이제 다 읽었으니 읽음 상태 0으롭 바꿔주기 위한 구문
                    if(Integer.parseInt(find("select state from Map2 where story_id="+s_id+" and map1_id="+m_id+" and map2_id="+m2_id)) == 1) {
                        database.execSQL("update Map2 set state = 0 where map1_id=" + m_id + " and story_id=" + s_id + " and map2_id="+m2_id);
                        //근처에 도달해야 열리는 스토리를 제외하고 다음 스토리를 열어주기 위해 다음 스토리를 찾는 조건 구문
                        int j = m2_id + 1;
                        while (j <= 3) {
                            if (Integer.parseInt(find("select state from Map2 where story_id="+s_id+" and map1_id="+m_id+" and map2_id="+j)) == 2)
                                break;
                            j++;
                        }
                        //다음스토리 상태값을 변환해주는 조건구문
                        if (j <= 3)
                            database.execSQL("update Map2 set state = 1 where map1_id=" + m_id + " and story_id=" + s_id + " and map2_id="+j);
                    }
                    Intent intent = new Intent(StoryPlayActivity.this, MapActivity.class);
                    intent.putExtra("p_id", s_id);
                    intent.putExtra("m_id", m_id);
                    startActivity(intent);
                }
                else{
                    tv.setText(find("select content from Views where story_id="+s_id+" and map1_id="+m_id+" and map2_id="+m2_id+" and view_id="+i));
                }
            }
        });

    }

    //sql 구문에 따라 결과를 돌려주는 메소드
    public String find(String sql){
        Cursor result = database.rawQuery(sql, null);
        String r = "";
        result.moveToFirst();
        while(!result.isAfterLast()){
            r = result.getString(0);
            result.moveToNext();
        }
        result.close();
        return r;
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
