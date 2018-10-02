package com.example.tourproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

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
                intent = new Intent(MainActivity.this, GachaActivity.class);
                startActivity(intent);
                break;
        }
    }
}
