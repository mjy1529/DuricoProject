package com.example.tourproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//layout : activity_story_list, viewflipper1, viewflipper2, viewflipper3, listview_item_storylist
//src : ViewFlipperAction, ItemDataStoryList, ListAdapterStoryList
//anim, menu
public class StoryListActivity extends AppCompatActivity implements  ViewFlipperAction.ViewFlipperCallback{

    Toolbar toolbar; //툴바설정

    //베스트 이미지 화면을 위한 변수
    ViewFlipper flipper;
    List<ImageView> indexes;

    //db를 이용하기 위한 변수
    SQLiteDatabase database = null;
    String dbName = "test1.db";
    ArrayList<String> arrlist = null; //Storys table에 있는 모든 레코드의 title을 담는 변수
    ArrayList<String> arr_id_list = null;//Storys table에 있는 모든 레코드의 story_id를 담는 변수
    ArrayList<String> arr_storycnt_list = null;

    //list를 이용하기 위한 변수
    ArrayAdapter<String> Adapter;
    ListView list;

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StoryListActivity.this, MainSelectActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    //시작
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        Toolbar  myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.home)).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        actionBar.setHomeAsUpIndicator(new BitmapDrawable(bitmap));

        //db 생성 메소드
        createDatabase();

        arrlist = new ArrayList<String>();
        arr_id_list = new ArrayList<String>();
        arr_storycnt_list = new ArrayList<String>();
        //selec을 이용해 Storys table에서 title와 story_id를 얻어오는 메소드
        selectData();
        for(int i = 0; i < arr_id_list.size(); i++)
            selectcnt(Integer.parseInt(arr_id_list.get(i)));
        ArrayList<ItemDataStoryList> oData = new ArrayList<>();
        for (int i=0; i<arr_id_list.size(); i++)
        {
            ItemDataStoryList oItem = new ItemDataStoryList();
            oItem.strTitle = arrlist.get(i);
            oItem.strDate = arr_storycnt_list.get(i) + "개의 이야기가 있습니다.";
            oData.add(oItem);
        }
        //Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrlist);
        list = (ListView) findViewById(R.id.l_view);
        ListAdapterStoryList oAdapter = new ListAdapterStoryList(oData);
        list.setAdapter(oAdapter);
        Log.i("dddddddd","deew3dddd");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //선택된 위치를 저장하는 변수
                final Integer selectedPos = i;

                String position = arr_id_list.get(selectedPos);
                Intent intent = new Intent(StoryListActivity.this, MapActivity.class);
                //story_id를 넘겨준다
                intent.putExtra("p_id", position);
                intent.putExtra("m_id", 1);
                intent.putExtra("p_title", arrlist.get(Integer.parseInt(position) - 1));
                startActivity(intent);
            }
        });

        //UI
        flipper = (ViewFlipper) findViewById(R.id.flipper_bestImage);
        ImageView index0 = (ImageView)findViewById(R.id.imageView_imgIndex0);
        ImageView index1 = (ImageView)findViewById(R.id.imageView_imgIndex1);
        ImageView index2 = (ImageView)findViewById(R.id.imageView_imgIndex2);
        //인덱스리스트
        indexes = new ArrayList<>();
        indexes.add(index0);
        indexes.add(index1);
        indexes.add(index2);
        //xml을 inflate 하여 flipper view에 추가하기
        //inflate
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.viewflipper1, flipper, false);
        View view2 = inflater.inflate(R.layout.viewflipper2, flipper, false);
        View view3 = inflater.inflate(R.layout.viewflipper3, flipper, false);
        //inflate 한 view 추가
        flipper.addView(view1);
        flipper.addView(view2);
        flipper.addView(view3);

        //리스너설정 - 좌우 터치시 화면넘어가기
        flipper.setOnTouchListener(new ViewFlipperAction(this, flipper));
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
        Log.i("ddddd", sql);
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
            database.execSQL("INSERT INTO Storys VALUES(1, '조선건국1');");
            database.execSQL("INSERT INTO Storys VALUES(2, '3.1운동');");
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

    //db 생성 메소드
    public void createDatabase(){
        File file = new File(getFilesDir(), dbName);
        try{
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            //db에 있는 table을 생성하는 메소드
            createTable();
        }catch (SQLiteException e){
            e.printStackTrace();
        }

    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();;
        database.close();
    }

    //베스트 이미지 화면을 위한 메소드
    @Override
    public void onFlipperActionCallback(int position){
        for(int i=0; i<indexes.size(); i++){
            ImageView index = indexes.get(i);
            //현재화면의 인덱스 위치면 노란색
            if(i == position){
                index.setImageResource(R.drawable.c_yellow);
            }
            //그외
            else{
                index.setImageResource(R.drawable.yellow);
            }
        }
    }
}
