package com.example.tourproject.StoryPlay;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.CardBox.CardData;
import com.example.tourproject.MainActivity;
import com.example.tourproject.Map.MapActivity;
import com.example.tourproject.Map.VerticalAdapter;
import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.StoryList.StoryListActivity;
import com.example.tourproject.Util.Application;
import com.example.tourproject.Util.CardManager;
import com.example.tourproject.Util.StoryPlayManager;
import com.example.tourproject.Util.UserManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryPlayActivity extends AppCompatActivity {

    int i = 0; //current page
    int v_cnt; //view 개수

    int map2_id; //map2_id

    TextView tv; //내용을 보여주는 textView
    TextView n;
    ImageView imageView; //스토리 사진을 보여주는 imageView
    ArrayList<StoryPlayData> storyPlayList;

    String log;
    int CHECK_NUM = 0;
    Button logbtn;

    public static final String TAG = "StoryPlay";
    private Dialog MyDialog;
    NetworkService networkService;
    String user_id;

    ArrayList<CardData> popUpCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        Intent intent = getIntent();
        map2_id = intent.getIntExtra("map2_id", 0);

        init();
        getStoryPlayList(map2_id);
        setStoryPlay();

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
                        AlertDialog.Builder alert = new AlertDialog.Builder(StoryPlayActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = getBaseContext().getPackageManager().
                                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
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

    public void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar2, null);
        actionBar.setCustomView(mCustomView);

        tv = (TextView) findViewById(R.id.content);
        n = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        log = "";
        logbtn = (Button) findViewById(R.id.log);

        networkService = Application.getInstance().getNetworkService();
        user_id = UserManager.getInstance().getUserId();
    }

    public void getStoryPlayList(int map2_id) {
        StoryPlayManager storyPlayDataManager = StoryPlayManager.getInstance();
        ArrayList<StoryPlayData> allStoryPlayList = storyPlayDataManager.getStoryPlayList();

        storyPlayList = new ArrayList<>();

        for (int i = 0; i < allStoryPlayList.size(); i++) {
            if (allStoryPlayList.get(i).getMap2_id() == map2_id) {
                storyPlayList.add(allStoryPlayList.get(i));
            }
        }
    }

    public void isName(String name) {
        if (!name.equals("null")) {
            n.setText(name);
            n.setVisibility(View.VISIBLE);
            log += name + " : ";
        } else
            n.setVisibility(View.INVISIBLE);
    }

    public void setStoryPlay() {
        v_cnt = storyPlayList.size();
        isName(storyPlayList.get(0).getPerson_name());

        tv.setText(storyPlayList.get(0).getPlay_content());
        log += storyPlayList.get(0).getPlay_content() + "\n\n";

        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + storyPlayList.get(0).getPlay_image_url())
                .into(imageView);

        //해당 이야기에 맞는 카드가 있는지 검색하기
        popUpCards = findCard();

//        //해당 이야기에 맞는 카드가 있을 때 처리
//        if(popUpCards != null) {
//            for (int i = 0; i < popUpCards.size(); i++) {
//                if (popUpCards.get(i).getMap2_id() != 0) {
//                    if (popUpCards.get(i).getCard_category().equals("people")) {
//                        showPeopleCardDialog(popUpCards.get(i));
//                        updateOpenPeopleCard(popUpCards.get(i)); //오픈 인물카드 업데이트
//                    } else if (popUpCards.get(i).getCard_category().equals("story")) {
//                        showStoryCardDialog(popUpCards.get(i));
//                        updateOpenStoryCard(popUpCards.get(i)); //오픈 스토리카드 업데이트
//                    }
//                }
//            }
//        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i < v_cnt) { // 현재 페이지가 전체 뷰보다 작을 때 다음 view 보여주기
                    isName(storyPlayList.get(i).getPerson_name());
                    tv.setText(storyPlayList.get(i).getPlay_content());
                    log += storyPlayList.get(i).getPlay_content() + "\n\n";
                    Glide.with(StoryPlayActivity.this)
                            .load(Application.getInstance().getBaseImageUrl() + storyPlayList.get(i).getPlay_image_url())
                            .into(imageView);

                } else if (i == v_cnt) { // 마지막 페이지일 때
                    //DB map2 상태 업데이트
                    updateMap2State(map2_id);

                    //UserManager의 map2State 업데이트
                    UserManager userManager = UserManager.getInstance();
                    for (int i = 0; i < userManager.getMap2StateList().size(); i++) {
                        //첫번째 이야기이고 현재 map2_id와 일치하는 userManager의 map2StateList를 업데이트 시킨다.
                        if (userManager.getMap2StateList().get(i).getMap2_id() == map2_id
                                && userManager.getMap2StateList().get(i).getMap2_position() == 1) {
                            userManager.getMap2StateList().get(i).setMap2_state(0);
                            userManager.getMap2StateList().get(i + 2).setMap2_state(1);
                            break;
                        }
                    }
                    MapActivity.mAdapter.notifyDataSetChanged();

                    if (popUpCards == null) {
                        finish();
                    } else {
                        if (popUpCards.get(0).getCard_category().equals("people")) {
                            updateOpenPeopleCard(popUpCards.get(0)); //오픈 인물카드 업데이트
                            showPeopleCardDialog(popUpCards.get(0));
                        } else if (popUpCards.get(0).getCard_category().equals("story")) {
                            updateOpenStoryCard(popUpCards.get(0)); //오픈 스토리카드 업데이트
                            showStoryCardDialog(popUpCards.get(0));
                        }
                    }

                } else if (i == (v_cnt + 1)) {
                    if (popUpCards.size() == 2) {
                        if (popUpCards.get(1).getCard_category().equals("people")) {
                            updateOpenPeopleCard(popUpCards.get(1)); //오픈 인물카드 업데이트
                            showPeopleCardDialog(popUpCards.get(1));
                        } else if (popUpCards.get(1).getCard_category().equals("story")) {
                            updateOpenStoryCard(popUpCards.get(1)); //오픈 스토리카드 업데이트
                            showStoryCardDialog(popUpCards.get(1));
                        }
                    } else {
                        finish();
                    }

                } else {
                    finish();
                }
            }
        });
    }

    //액션바 홈버튼 동작을 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StoryPlayActivity.this, MapActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            Intent intent = new Intent(getApplicationContext(), StoryListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v.getId() == R.id.log) {
            if (CHECK_NUM == 0) {
                logbtn.setBackgroundResource(R.drawable.log_1);
                CHECK_NUM = 1;
                MyCustomAlertDialog();
            }
        }
    }

    public ArrayList<CardData> findCard() {
        ArrayList<CardData> popUpCards = new ArrayList<>();

        ArrayList<CardData> peopleCardList = CardManager.getInstance().getPeopleCardList(); //인물카드
        ArrayList<CardData> storyCardList = CardManager.getInstance().getStoryCardList(); //스토리카드

        //인물카드들 중에 검색하여 popUpCards에 추가1
        for (int i = 0; i < peopleCardList.size(); i++) {
            if (peopleCardList.get(i).getMap2_id() != 0 && peopleCardList.get(i).getMap2_id() == map2_id) {
                popUpCards.add(peopleCardList.get(i));
            }
        }

        //스토리카드들 중에 검색하여 popUpCards에 추가
        for (int i = 0; i < storyCardList.size(); i++) {
            if (storyCardList.get(i).getMap2_id() == map2_id) {
                popUpCards.add(storyCardList.get(i));
            }
        }

        if (popUpCards.size() == 0) {
            return null;
        } else {
            return popUpCards;
        }
    }

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(StoryPlayActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong2);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView logcontent = (TextView) MyDialog.findViewById(R.id.logcontent);
        logcontent.setMovementMethod(new ScrollingMovementMethod());
        logcontent.setText(log);

        MyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                CHECK_NUM = 0;
                logbtn.setBackgroundResource(R.drawable.log);
            }
        });
        /*
        logcontent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CHECK_NUM = 0;
                log.setBackgroundResource(R.drawable.log);
                MyDialog.cancel();
            }
        });*/
        MyDialog.show();
    }

    //map2에 해당하는 인물카드 팝업 띄우기
    public void showPeopleCardDialog(CardData cardData) {
        MyDialog = new Dialog(StoryPlayActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView cardName = (TextView) MyDialog.findViewById(R.id.cardName);
        TextView cardcontent = (TextView) MyDialog.findViewById(R.id.cardContent);
        ImageView cardimage = (ImageView) MyDialog.findViewById(R.id.gacha_card);
        LinearLayout card = (LinearLayout) MyDialog.findViewById(R.id.pickview);

        cardName.setText(cardData.getCard_name());
        cardcontent.setText(cardData.getCard_description());
        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + cardData.getCard_image_url())
                .into(cardimage);
        Toast.makeText(this, "gacha!", Toast.LENGTH_SHORT).show();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });
        MyDialog.show();
    }

    //map2에 해당하는 스토리카드 팝업 띄우기
    public void showStoryCardDialog(CardData cardData) {
        MyDialog = new Dialog(StoryPlayActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialog3);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView cardImage = (ImageView) MyDialog.findViewById(R.id.pickCard_noDesc);
        LinearLayout card = (LinearLayout) MyDialog.findViewById(R.id.pickview_noDesc);
        Toast.makeText(this, "gacha!", Toast.LENGTH_SHORT).show();

        Glide.with(this)
                .load(Application.getInstance().getBaseImageUrl() + cardData.getCard_image_url())
                .into(cardImage);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });
        MyDialog.show();
    }

    //뽑은 카드 상태(close -> open) 업데이트
    public void updateOpenPeopleCard(final CardData cardData) {
        NetworkService networkService = Application.getInstance().getNetworkService();
        String user_id = UserManager.getInstance().getUserId();
        Call<String> request = networkService.updatePeopleCardState(user_id, cardData.getCard_idx());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (!UserManager.getInstance().getOpenPeopleCardList().contains(cardData.getCard_idx())) {
                        UserManager.getInstance().getOpenPeopleCardList().add(cardData.getCard_idx());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateOpenStoryCard(final CardData cardData) {
        Call<String> request = networkService.updateStoryCardState(user_id, cardData.getCard_idx());
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!UserManager.getInstance().getOpenStoryCardList().contains(cardData.getCard_idx())) {
                    UserManager.getInstance().getOpenStoryCardList().add(cardData.getCard_idx());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateMap2State(final int map2_id) {
        Call<String> request = networkService.updateMap2State(user_id, map2_id);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("맵 상태 변경", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("맵 상태 변경 실패", t.getMessage());
            }
        });
    }
}
