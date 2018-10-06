package com.example.tourproject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GachaActivity extends AppCompatActivity{

    private static final long START_TIME_IN_MILLIS = 20000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;

    Dialog MyDialog;
    TextView cardcontent;
    LinearLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha);

        mTextViewCountDown = findViewById(R.id.txt_time);

        mButtonStartPause = findViewById(R.id.btn_pick);

        mButtonStartPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!mTimerRunning) {
                    resetTimer();
                    startTimer();
                    MyCustomAlertDialog();
                }
            }
        });

    }

    private void startTimer(){
        mEndTime = SystemClock.elapsedRealtime() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                resetTimer();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText(){
        int hours = (int)(mTimeLeftInMillis / (60 * 60 * 1000)) % 24;
        int minutes = (int)(mTimeLeftInMillis / (60 *1000)) % 60;
        int seconds = (int)(mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private  void updateButtons(){
        if(mTimerRunning){
            mButtonStartPause.setEnabled(false);
        }else{
            updateCountDownText();
            mButtonStartPause.setEnabled(true);
        }
    }

    @Override
    protected  void onStop(){
        super.onStop();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();;
    }

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if(mTimerRunning){
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - SystemClock.elapsedRealtime();

            if(mTimeLeftInMillis < 1000){
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                resetTimer();
            }else{
                startTimer();
            }
        }
    }

    public int pick(){
        double r  = Math.random(); //{0.0 - 1.0}
        double dr = r * 100.0f; // {0.0 - 100.0}

        double p[] = { 5.0f, 15.0f, 15.0f, 30.0f, 35.0f }; //4, 3, 2, 1, 0

        double cumulative = 0.0f;
        int i;
        for(i=0; i<5; i++)
        {
            cumulative += p[i];
            if(dr <= cumulative)
                break;
        }
        return 4-i;
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(GachaActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong);
        MyDialog.setTitle("My Custom Dialog");

        cardcontent = (TextView)MyDialog.findViewById(R.id.cardContent);
        cardcontent.setText(Integer.toString(pick()));
        card = (LinearLayout)MyDialog.findViewById(R.id.pickview);

        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyDialog.cancel();
            }
        });
        MyDialog.show();
    }
}
