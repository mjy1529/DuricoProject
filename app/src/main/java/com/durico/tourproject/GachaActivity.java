package com.durico.tourproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.durico.tourproject.CardBox.CardBoxActivity;
import com.durico.tourproject.CardBox.CardData;
import com.durico.tourproject.Map.MapActivity;
import com.durico.tourproject.Network.NetworkService;
import com.durico.tourproject.StoryList.StoryListActivity;
import com.durico.tourproject.StoryPlay.StoryPlayActivity;
import com.durico.tourproject.Util.Application;
import com.durico.tourproject.Util.CardManager;
import com.durico.tourproject.Util.UserManager;
import com.instacart.library.truetime.TrueTime;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class GachaActivity extends AppCompatActivity{

    private static final long START_TIME_IN_MILLIS = 86400000;

    private final int REQUEST_WIDTH = 512;
    private final int REQUEST_HEIGHT = 512;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long EndTime;
    private long mEndTime;

    Dialog MyDialog;
    ImageView cardimage;
    TextView cardcontent;
    TextView cardName;
    LinearLayout card;

    TextView pe;

    private final MyHandler mHandler = new MyHandler(this);
    private Thread backgroundThread;
    private boolean running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha);

        doActionbar();

        backgroundThread = new Thread(new BackgroundThread());
        setRunning(true);
        backgroundThread.start();

        try {
            backgroundThread.join();
        }catch(InterruptedException e) {
            Log.i("TRUETIMEWAIT", "ERROR");
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
                }else{
                    Toast.makeText(GachaActivity.this, "24시간에 한번만 뽑기가 가능합니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback()
                {
                    @Override
                    public void onAvailable( Network network )
                    {
                        //네트워크 연결됨
                    }

                    @Override
                    public void onLost( Network network )
                    {
                        //네트워크 끊어짐
                        AlertDialog.Builder alert = new AlertDialog.Builder(GachaActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.finishAffinity(GachaActivity.this);
                            }
                        });
                        try {
                            alert.show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }
                    }
                } );
    }

    public void doActionbar(){
        //액션바-------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);
        pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            finish();
        }
    }

    void setRunning(boolean b) {
        running = b;
    }

    public class BackgroundThread implements Runnable {

        public void run() {
            try {
                TrueTime.build().withNtpHost("time.google.com").initialize();
                Log.e("TrueTime", "Connected");
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.e("TrueTime", "SocketError");
            }catch(IOException e){
                e.printStackTrace();
                Log.e("TrueTime", "Error");
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<GachaActivity> mActivity;

        public MyHandler(GachaActivity activity) {
            mActivity = new WeakReference<GachaActivity>(activity);
        }

    }

    private void startTimer(){

        //mEndTime = SystemClock.elapsedRealtime() + mTimeLeftInMillis;
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
        //updateButtons();
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        //updateButtons();
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
        //editor.putLong("endTime", mEndTime);
        editor.putLong("endTime", EndTime);
        editor.apply();
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
                backgroundThread = new Thread(new BackgroundThread());
                setRunning(true);
                backgroundThread.start();
                try {
                    backgroundThread.join();
                }catch(InterruptedException e) {
                    Log.i("TRUETIMEWAIT", "ERROR");
                    e.printStackTrace();
                }

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
                mTimerRunning = prefs.getBoolean("timerRunning", false);

                updateCountDownText();
                //updateButtons();

                if (mTimerRunning) {
                    EndTime = prefs.getLong("endTime", 0);
                    //mEndTime = prefs.getLong("endTime", 0);
                    mTimeLeftInMillis = EndTime - TrueTime.now().getTime();
                    //mTimeLeftInMillis = mEndTime - SystemClock.elapsedRealtime();
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

    public int pick(){
        double r  = Math.random(); //{0.0 - 1.0}
        double dr = r * 100.0f; // {0.0 - 100.0}

        double p[] = { 5.0f, 15.0f, 80.0f }; //2, 1, 0

        double cumulative = 0.0f;
        int i;
        for(i=0; i<3; i++)
        {
            cumulative += p[i];
            if(dr <= cumulative)
                break;
        }
        return 2-i;
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(GachaActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        cardName = (TextView) MyDialog.findViewById(R.id.cardName);
        cardcontent = (TextView)MyDialog.findViewById(R.id.cardContent);
        cardcontent.setText(Integer.toString(pick()));

        cardimage = (ImageView)MyDialog.findViewById(R.id.gacha_card);

        // ******* 뽑기 이미지 띄우는 부분 ******* //
        CardData gachaCardData = getGachaCard(); //뽑힌 카드 데이터
        if(gachaCardData != null) {
            //카드 이미지 세팅
            Glide.with(this)
                    .load(Application.getInstance().getBaseImageUrl() + gachaCardData.getCard_image_url())
                    .into(cardimage);
            cardName.setText(gachaCardData.getCard_name()); //카드 이름
            cardcontent.setText(gachaCardData.getCard_description()); //카드 설명
            Toast.makeText(this, "gacha!", Toast.LENGTH_SHORT).show();

            updateUserOpenCard(gachaCardData);

        } else { //더이상 뽑을 카드가 없을 때?
            cardimage.setImageResource(R.drawable.p_1);
            cardcontent.setText("더 이상 뽑을 카드가 없습니다.");
        }
        // *********************************** //

        card = (LinearLayout)MyDialog.findViewById(R.id.pickview);

        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyDialog.cancel();
            }
        });
        MyDialog.show();
    }

    public CardData getGachaCard() {
        //인물카드를 받아와서 사용자 카드 중 close 상태인 카드들만 따로 리스트(gachaList)를 만든다.
        ArrayList<Integer> userOpenIdxList = UserManager.getInstance().getOpenPeopleCardList();
        ArrayList<CardData> gachaList = CardManager.getInstance().getGachaCardList();

        for (int i = 0; i < gachaList.size(); i++) {
            for (int j = 0; j < userOpenIdxList.size(); j++) {
                if (gachaList.get(i).getCard_idx() == userOpenIdxList.get(j)) {
                    gachaList.remove(gachaList.get(i));
                }
            }
        }

        if (gachaList.size() != 0) {
            ArrayList<CardData> gachaList_0 = new ArrayList<>();
            ArrayList<CardData> gachaList_1 = new ArrayList<>();
            ArrayList<CardData> gachaList_2 = new ArrayList<>();

            //gachaList 중 확률이 높은(0)을 우선한다.
            for (int i = 0; i < gachaList.size(); i++) {
                if (gachaList.get(i).getGacha() == 0) {
                    gachaList_0.add(gachaList.get(i));
                } else if (gachaList.get(i).getGacha() == 1) {
                    gachaList_1.add(gachaList.get(i));
                } else {
                    gachaList_2.add(gachaList.get(i));
                }
            }

            if (gachaList_0.size() != 0) {
                //Collections.shuffle(gachaList_0); //랜덤으로 뽑고 싶으면 주석 해제하기
                return gachaList_0.get(0);
            } else if (gachaList_1.size() != 0) {
                //Collections.shuffle(gachaList_0);
                return gachaList_1.get(0);
            } else {
                //Collections.shuffle(gachaList_0);
                return gachaList_2.get(0);
            }

        } else {
            return null;
        }
    }

    //뽑은 카드 상태(close -> open) 업데이트
    public void updateUserOpenCard(final CardData cardData) {
        NetworkService networkService = Application.getInstance().getNetworkService();
        String user_id = UserManager.getInstance().getUserId();
        Call<String> request = networkService.updatePeopleCardState(user_id, cardData.getCard_idx());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    int open_people_card_cnt = Integer.parseInt(response.body());
                    //오픈된 인물 카드 인덱스 받아오기
                    UserManager.getInstance().getOpenPeopleCardList().add(cardData.getCard_idx());
                    pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pecardCnt:
            case R.id.pcardCnt:
            case R.id.scardCnt:
                Intent intent = new Intent(GachaActivity.this, CardBoxActivity.class);
                startActivity(intent);
        }
    }

}
