package com.example.tourproject.StoryList;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.tourproject.Util.Application;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Util.StoryListManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;


//activity_story_list
//layout : horizon_recycler_items
//src : HorizonAdapter, StoryData, HorizonViewHolder (http://diordna.tistory.com/19?category=677940), RecyclerItemClickListener
public class StoryListActivity extends AppCompatActivity {

    RecyclerView mHorizonView;
    HorizonAdapter horizonAdapter;
    LinearLayoutManager mLayoutManger;

<<<<<<< HEAD
    public final static String TAG = "StoryList";

=======
    //시작
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

<<<<<<< HEAD
        init();
        setRecyclerView();
    }

    public void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
=======
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

        //db 생성 메소드
        createDatabase();
        arrlist = new ArrayList<String>();
        arr_id_list = new ArrayList<String>();
        arr_storycnt_list = new ArrayList<String>();

        //selec을 이용해 Storys table에서 title와 story_id를 얻어오는 메소드
        selectData();

        for(int i = 0; i < arr_id_list.size(); i++)
            selectcnt(Integer.parseInt(arr_id_list.get(i)));
        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        ArrayList<HorizonData> data = new ArrayList<>();
        for (int i=0; i<arr_id_list.size(); i++)
            data.add(new HorizonData(R.drawable.a_2,arrlist.get(i)));
        final LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHorizonView.setLayoutManager(mLayoutManger);
        mAdapter = new HorizonAdapter();
        mAdapter.setData(data);
        mHorizonView.setAdapter(mAdapter);
        mHorizonView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mHorizonView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String selectedID = arr_id_list.get(position);
                Intent intent = new Intent(StoryListActivity.this, MapActivity.class);
                //story_id를 넘겨준다
                intent.putExtra("p_id", selectedID);
                intent.putExtra("m_id", "1");
                startActivity(intent);
            }
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

<<<<<<< HEAD
    public void setRecyclerView() {
        mHorizonView = (RecyclerView) findViewById(R.id.horizon_list);
        mLayoutManger = new LinearLayoutManager(StoryListActivity.this);
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHorizonView.setLayoutManager(mLayoutManger);
        mHorizonView.setHasFixedSize(true);
=======
    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            onBackPressed();
        }
    }

    //selec을 이용해 Storys table에서 title와 story_id를 얻어오는 메소드
    public void selectData(){
        String sql = "select * from Storys";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            arr_id_list.add(result.getString(0));
            arrlist.add(result.getString(1));
            result.moveToNext();
        }
        result.close();
    }

    public void selectcnt(int id){
        String sql = "select count(*) as cnt from Map2 where story_id="+id;
        Cursor result = database.rawQuery(sql, null);
        String r = "";
        result.moveToFirst();
        while(!result.isAfterLast()){
            arr_storycnt_list.add(result.getString(0));
            result.moveToNext();
        }
            result.close();
    }

    //db에 있는 table을 생성하는 메소드
    public void createTable(){
        try{
            database.execSQL("CREATE TABLE Storys (story_id INTEGER PRIMARY KEY, title TEXT);");
            database.execSQL("CREATE TABLE Map1 (map1_id INTEGER PRIMARY KEY, story_id INTEGER, state INTEGER);");
            database.execSQL("CREATE TABLE Map2 (map2_id INTEGER, title TEXT, explantion TEXT, state INTEGER, story_id INTEGER, map1_id INTEGER);");
            database.execSQL("CREATE TABLE Views (view_id INTEGER, content TEXT, story_id INTEGER, map1_id INTEGER, map2_id INTEGER);");
            database.execSQL("INSERT INTO Storys VALUES(1, '조\n선\n건\n국\n1');");
            database.execSQL("INSERT INTO Storys VALUES(2, '3\n.\n1\n운\n동');");
            database.execSQL("INSERT INTO Storys VALUES(3, '조\n선\n건\n국\n2');");
            database.execSQL("INSERT INTO Storys VALUES(4, '경\n복\n궁');");
            database.execSQL("INSERT INTO Map1 VALUES(1, 1, 1);");
            database.execSQL("INSERT INTO Map1 VALUES(2, 1, 0);");
            database.execSQL("INSERT INTO Map1 VALUES(3, 1, 0);");
            database.execSQL("INSERT INTO Map2 VALUES(1, '새로운 정치세력의 등장', '새로운 정치세력이 등장했다.', 1, 1, 1);");
            database.execSQL("INSERT INTO Map2 VALUES(2, '정도전의 유배', '새로운 정치세력이 등장했다.', 3, 1, 1);");
            database.execSQL("INSERT INTO Map2 VALUES(3, '혁명을 일으키기로 마음먹은 정도전', '새로운 정치세력이 등장했다.', 2, 1, 1);");
            database.execSQL("INSERT INTO Map2 VALUES(1, '최영과 이성계', '새로운 정치세력이 등장했다.', 1, 1, 2);");
            database.execSQL("INSERT INTO Map2 VALUES(2, '혁명을 일으키기로 마음먹은 정도전', '새로운 정치세력이 등장했다.', 3, 1, 2);");
            database.execSQL("INSERT INTO Views VALUES(1, '권문세족이 등장했다', 1, 1, 1);");
            database.execSQL("INSERT INTO Views VALUES(2, '신진사대부가 등장했다', 1, 1, 1);");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
>>>>>>> 88d08faf93ce6ed9a4e619a5442e90279e2ac043

        if (StoryListManager.getInstance().getStoryList().size() != 0) {
            horizonAdapter = new HorizonAdapter(StoryListActivity.this, StoryListManager.getInstance().getStoryList());
            mHorizonView.setAdapter(horizonAdapter);
        }
    }

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
