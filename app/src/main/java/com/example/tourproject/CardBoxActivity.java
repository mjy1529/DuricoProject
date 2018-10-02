package com.example.tourproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class CardBoxActivity extends AppCompatActivity implements ViewFlipperAction.ViewFlipperCallback {

    ViewFlipper flipper;

    RecyclerView mHorizonView;
    RecyclerView mHorizonView2;
    GridAdapter mAdapter;

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CardBoxActivity.this, MainActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_box);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.home)).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        actionBar.setHomeAsUpIndicator(new BitmapDrawable(bitmap));


        //UI
        flipper = (ViewFlipper) findViewById(R.id.flipper_card);
        //xml을 inflate 하여 flipper view에 추가하기
        //inflate
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        FrameLayout layoutExample = (FrameLayout)inflater.inflate(R.layout.viewflipper1, null);

        mHorizonView = (RecyclerView) layoutExample.findViewById(R.id.grid_list);
        ArrayList<GridData> data = new ArrayList<>();
        for (int i=0; i<5; i++)
            data.add(new GridData(R.drawable.gyeongbokpalace));
        GridLayoutManager mLayoutManger = new GridLayoutManager(this, 3);
        mHorizonView.setLayoutManager(mLayoutManger);
        mAdapter = new GridAdapter();
        mAdapter.setData(data);
        mHorizonView.setAdapter(mAdapter);

        Log.i("제발나와라",Integer.toString(mAdapter.getItemCount()));
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
    @Override
    public void onFlipperActionCallback(int position){

    }
}
