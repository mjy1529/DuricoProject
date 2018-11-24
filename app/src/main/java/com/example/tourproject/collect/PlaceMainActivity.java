package com.example.tourproject.collect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tourproject.CardBox.CardBoxActivity;
import com.example.tourproject.MainActivity;
import com.example.tourproject.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;
import static com.example.tourproject.collect.MyJobService.data;
import static com.example.tourproject.collect.MyJobService.data2;
import static com.example.tourproject.collect.MyJobService.mapx;
import static com.example.tourproject.collect.MyJobService.mapy;
import static com.example.tourproject.collect.MyJobService.find;
import static com.example.tourproject.collect.MyJobService.find2000;


public class PlaceMainActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    ListView listView;
    ListView listView2;
    TextView text;
    ListviewAdapter adapter;
    ListviewAdapter adapter2;
    String key = "1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Button home = (Button) findViewById(R.id.home);

        listView = (ListView)findViewById(R.id.listview300);
        listView2 = (ListView)findViewById(R.id.listview2000);
        if(data2.size() == 0 && mapy == 0){
            Log.i("data2가 없습니다.", ",,");
            data.clear();
            data2.clear();
            //setGps();
        }

        Log.i("data2 전달됐어요", String.valueOf(data2.size()));
        adapter=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter2=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data2);
        adapter2.notifyDataSetChanged();
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), overView.class);
                //intent.putExtra("img", data.get(position).getIcon());
                intent.putExtra("title", data.get(position).getName());
                intent.putExtra("content_id", data.get(position).getContent_id());
                intent.putExtra("contentType_id", data.get(position).getContentType_id());
                intent.putExtra("mapx", data.get(position).getMapx());
                intent.putExtra("mapy", data.get(position).getMapy());
                startActivity(intent);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), overView2000.class);
                //intent.putExtra("img", data.get(position).getIcon());
                intent.putExtra("title", data2.get(position).getName());
                intent.putExtra("content_id", data2.get(position).getContent_id());
                intent.putExtra("contentType_id", data2.get(position).getContentType_id());
                intent.putExtra("mapx", data2.get(position).getMapx());
                intent.putExtra("mapy", data2.get(position).getMapy());
                startActivity(intent);
            }
        });
    }
    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            /*Intent intent = new Intent(PlaceMainActivity.this, MainActivity.class);
            startActivity(intent);*/
            onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PlaceMainActivity.this, MainActivity.class);
                startActivity(intent);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    public void onClick(View v) {

    }
   /*
    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("지금 리스너 들어왔어요", "PlaceMainActivity 리스너");
            if (location != null) {
                mapy = location.getLatitude();
                mapx = location.getLongitude();
                Log.i("지금 주소 받았어요", "PlaceMainActivity 리스너");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //listView = (ListView)findViewById(R.id.list);
                    Log.i("리스너 스레드 들어왔어요",Double.toString(mapx));
                    Log.i("여기나야관련들어왔어요",Double.toString(mapy));
                    find(mapx, mapy, 12);
                    find(mapx, mapy, 14);
                    find2000(mapx, mapy, 12);
                    find2000(mapx, mapy, 14);
                    if(data.size() == 0) {
                        Log.i("Place메인에서 data가 없습니다.", ",,");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //text.setText(data);
                            /*adapter=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            adapter2=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data2);
                            listView2.setAdapter(adapter);
                            adapter2.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    //Button을 클릭했을 때 자동으로 호출되는 callback method....

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
    //XmlPullParser를 이용하여 Naver 에서 제공하는 OpenAPI XML 파일 파싱하기(parsing)

    public void setGps() {
        Log.i("함수들어갑니다.","PlaceMainActivity setGPS()");
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        String provider = lm.getBestProvider(criteria, true);

        //String provider = LocationManager.PASSIVE_PROVIDER;
        lm.requestLocationUpdates(provider, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000 * 60 * 60, // 통지사이의 최소 시간간격 (miliSecond)
                500, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

    }*/
}//MainActivity class..

