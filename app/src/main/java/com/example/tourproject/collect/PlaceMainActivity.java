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
        if(data2.size() == 0){
            Log.i("data2가 없습니다.", ",,");
            data.clear();
            data2.clear();
            //setGps();
        }

        Log.i("data2 전달됐어요", String.valueOf(data2.size()));
        adapter = null;
        adapter2 = null;
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
    /*private final LocationListener mLocationListener = new LocationListener() {
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
                            adapter=new ListviewAdapter(PlaceMainActivity.this, R.layout.item, data);
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
    void find(double longi, double lati, int contentTypeNum) {
        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&mapX="+longi+"&mapY="+lati+"&radius=300&contentTypeId="+contentTypeNum;
        Log.i("함수들어갑니다.","PlaceMainActivity find()");
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is= url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            Bitmap imagesrc = null;
            String title = "";
            String content_id = "";
            String addr = "";
            String contentType_id = "";
            String mapx = "";
            String mapy = "";
            data.clear();
            xpp.next();
            Listviewitem item1 = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                //Log.i("관련들어왔어요","ddddddd");
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("addr1")){
                            xpp.next();
                            addr = xpp.getText();
                        }
                        else if(tag.equals("firstimage")){
                            xpp.next();
                            imagesrc = getImageBitmap(xpp.getText());
                        }
                        else if(tag.equals("contentid")){
                            xpp.next();
                            content_id = xpp.getText();
                        }
                        else if(tag.equals("contenttypeid")){
                            xpp.next();
                            contentType_id = xpp.getText();
                        }
                        else if(tag.equals("mapx")){
                            xpp.next();
                            mapx = xpp.getText();
                        }
                        else if(tag.equals("mapy")){
                            xpp.next();
                            mapy = xpp.getText();
                        }
                        else if(tag.equals("title")){
                            xpp.next();
                            title = xpp.getText();
                            if(title != null) {
                                item1 = new Listviewitem(imagesrc, title, content_id, addr, contentType_id, mapx, mapy);
                                data.add(item1);
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기
                        if(tag.equals("item")) //buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Auto-generated catch blocke.printStackTrace();
            Log.i("find 함수 끝냈다",Double.toString(mapy));
        }
    }//getXmlData method....
    void find2000(double longi, double lati, int contentTypeNum) {
        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&mapX="+longi+"&mapY="+lati+"&radius=2000&contentTypeId="+contentTypeNum;
        Log.i("함수들어갑니다.","PlaceMainActivity find2000()");
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is= url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            Bitmap imagesrc = null;
            String title = "";
            String content_id = "";
            String addr = "";
            String contentType_id = "";
            String mapx = "";
            String mapy = "";
            data2.clear();
            xpp.next();
            Listviewitem item1 = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                //Log.i("관련들어왔어요","ddddddd");
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("addr1")){
                            xpp.next();
                            addr = xpp.getText();
                        }
                        else if(tag.equals("firstimage")){
                            xpp.next();
                            imagesrc = getImageBitmap(xpp.getText());
                        }
                        else if(tag.equals("contentid")){
                            xpp.next();
                            content_id = xpp.getText();
                        }
                        else if(tag.equals("contenttypeid")){
                            xpp.next();
                            contentType_id = xpp.getText();
                        }
                        else if(tag.equals("mapx")){
                            xpp.next();
                            mapx = xpp.getText();
                        }
                        else if(tag.equals("mapy")){
                            xpp.next();
                            mapy = xpp.getText();
                        }
                        else if(tag.equals("title")){
                            xpp.next();
                            title = xpp.getText();
                            if(title != null) {
                                item1 = new Listviewitem(imagesrc, title, content_id, addr, contentType_id, mapx, mapy);
                                data2.add(item1);
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기
                        if(tag.equals("item")) //buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Auto-generated catch blocke.printStackTrace();
            Log.i("find 함수 끝냈다",Double.toString(mapy));
        }
    }//getXmlData method....
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

