package com.example.tourproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSelectActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_select);

        Button searchBtn = (Button) findViewById(R.id.searchPlace);
        Button selectBtn = (Button) findViewById(R.id.selectStory);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainSelectActivity.this, PlaceMainActivity.class);
                startActivity(intent);
            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainSelectActivity.this, StoryMainActivity.class);
                startActivity(intent2);
            }
        });
    }
}
