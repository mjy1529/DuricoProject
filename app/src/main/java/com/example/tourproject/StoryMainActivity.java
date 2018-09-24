package com.example.tourproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StoryMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_story);

        //스토리 선택 버튼
        Button btnStorySelect = (Button) findViewById(R.id.button_storySelect);
        btnStorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoryMainActivity.this, StoryListActivity.class);
                startActivity(intent);
            }
        });
    }
}
