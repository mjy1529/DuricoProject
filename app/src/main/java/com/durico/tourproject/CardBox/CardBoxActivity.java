package com.durico.tourproject.CardBox;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.durico.tourproject.Collect.PlaceMainActivity;
import com.durico.tourproject.MainActivity;
import com.durico.tourproject.Map.MapActivity;
import com.durico.tourproject.Network.NetworkService;
import com.durico.tourproject.R;
import com.durico.tourproject.SplashActivity;
import com.durico.tourproject.Util.UserManager;

import retrofit2.http.HEAD;

public class CardBoxActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    NetworkService networkService;
    CardResult cardResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_box);

        Intent intent = getIntent();
        int selected_fragment = intent.getIntExtra("selected_fragment", 2);

        doActionbar();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        View tab1 = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        tab1.setBackgroundResource(R.drawable.tab_layout);
        View tab2 = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);
        tab2.setBackgroundResource(R.drawable.tab_layout3);
        View tab3 = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(2);
        tab3.setBackgroundResource(R.drawable.tab_layout2);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        //네트워크 연결됨
                    }

                    @Override
                    public void onLost(Network network) {
                        //네트워크 끊어짐
                        AlertDialog.Builder alert = new AlertDialog.Builder(CardBoxActivity.this);
                        alert.setTitle("네트워크");
                        alert.setMessage("네트워크가 연결되지 않았습니다.");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.finishAffinity(CardBoxActivity.this);
                            }
                        });
                        try {
                            if(alert != null)
                                alert.show();
                        } catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }
                    }
                });
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            Log.i("ddddd", Integer.toString(sectionNumber));
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first_card, container, false);
            return rootView;
        }
    }

    public void doActionbar() {
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
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ThirdCardFragment();
                case 1:
                    return new SecondCardFragment();
                case 2:
                    return new FirstCardFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            Intent intent = new Intent(CardBoxActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void onClick(View v) { //카드박스에서는 아무 이벤트도 일어나지 않도록
        switch (v.getId()) {
            case R.id.pcardCnt:
            case R.id.pecardCnt:
            case R.id.scardCnt:
                break;
        }
    }
}
