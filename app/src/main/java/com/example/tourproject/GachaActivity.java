package com.example.tourproject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.instacart.library.truetime.TrueTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GachaActivity extends AppCompatActivity{

    private static final long START_TIME_IN_MILLIS = 86400000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long EndTime;
    private long mEndTime;

    Dialog MyDialog;
    TextView cardcontent;
    LinearLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha);
        ThreadB A = new ThreadB();
        A.start();
        synchronized(A) {
            try {
                // b.wait()메소드를 호출.
                // 메인쓰레드는 정지
                // ThreadB가 5번 값을 더한 후 notify를 호출하게 되면 wait에서 깨어남
                System.out.println("b가 완료될때까지 기다립니다.");
                A.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mTextViewCountDown = findViewById(R.id.txt_time);

            mButtonStartPause = findViewById(R.id.btn_pick);
            if (!mTimerRunning) {
                resetTimer();
            }
            mButtonStartPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mTimerRunning) {
                        Date noReallyThisIsTheTrueDateAndTime = TrueTime.now();
                        EndTime = noReallyThisIsTheTrueDateAndTime.getTime() + START_TIME_IN_MILLIS;
                        Log.i("date", noReallyThisIsTheTrueDateAndTime.toString());
                        mEndTime = SystemClock.elapsedRealtime() + START_TIME_IN_MILLIS;
                        Log.i("시작", Long.toString(mEndTime));
                        resetTimer();
                        startTimer();
                        MyCustomAlertDialog();
                    }
                }

            });
        }

    }


    private void startTimer(){

        //mEndTime = SystemClock.elapsedRealtime() + mTimeLeftInMillis;
        //Log.i("여기를 들어오는 거니??????", Long.toString(mTimeLeftInMillis));
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
        int hours = (int)(mTimeLeftInMillis / (60 * 60 * 1000));
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
        Log.i("여기 mendtime 함수", Long.toString(mEndTime));
        //editor.putLong("endTime", mEndTime);
        editor.putLong("endTime", EndTime);
        editor.apply();;
    }
    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
    @Override
    protected void onStart(){
        super.onStart();
        NetworkInfo mNetworkState = getNetworkInfo();
        if(mNetworkState != null && mNetworkState.isConnected()){
            if(mNetworkState.getType() == ConnectivityManager.TYPE_WIFI || mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE){
                ThreadB A = new ThreadB();
                A.start();
                synchronized(A) {
                    try {
                        // b.wait()메소드를 호출.
                        // 메인쓰레드는 정지
                        // ThreadB가 5번 값을 더한 후 notify를 호출하게 되면 wait에서 깨어남
                        System.out.println("b가 완료될때까지 기다립니다.");
                        A.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

                    mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
                    Log.i("여기를 들어오는 거야?", Long.toString(mTimeLeftInMillis));
                    mTimerRunning = prefs.getBoolean("timerRunning", false);

                    updateCountDownText();
                    updateButtons();

                    if (mTimerRunning) {
                        EndTime = prefs.getLong("endTime", 0);
                        //mEndTime = prefs.getLong("endTime", 0);
                        Log.i("여기를 들어오는 거니? mendtime", Long.toString(SystemClock.elapsedRealtime()));
                        mTimeLeftInMillis = EndTime - TrueTime.now().getTime();
                        //mTimeLeftInMillis = mEndTime - SystemClock.elapsedRealtime();
                        Log.i("여기를 들어오는 거니?", Long.toString(mTimeLeftInMillis));
                        if (mTimeLeftInMillis < 1000) {
                            mTimeLeftInMillis = 0;
                            mTimerRunning = false;
                            resetTimer();
                        } else {
                            startTimer();
                        }
                    }
                }
            }
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("네트워크");
            alert.setMessage("네트워크가 연결되지 않았습니다.");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
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
