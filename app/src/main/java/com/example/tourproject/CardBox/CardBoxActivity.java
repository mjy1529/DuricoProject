package com.example.tourproject.CardBox;


import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourproject.Network.NetworkService;
import com.example.tourproject.R;
import com.example.tourproject.Util.UserManager;

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

        doActionbar();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        View tab1 = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(0);
        tab1.setBackgroundResource(R.drawable.tab_layout);
        View tab2 = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(1);
        tab2.setBackgroundResource(R.drawable.tab_layout3);
        View tab3 = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(2);
        tab3.setBackgroundResource(R.drawable.tab_layout2);
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
        TextView pe = (TextView) findViewById(R.id.pecardCnt);
        pe.setText(String.valueOf(UserManager.getInstance().getOpen_people_card_cnt()));
        TextView s = (TextView) findViewById(R.id.scardCnt);
        s.setText(String.valueOf(UserManager.getInstance().getOpen_story_card_cnt()));
        TextView p = (TextView) findViewById(R.id.pcardCnt);
        p.setText(String.valueOf(UserManager.getInstance().getPlace_card_cnt()));
        //여기까지------------------------------
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
            finish();
        }
    }
}
