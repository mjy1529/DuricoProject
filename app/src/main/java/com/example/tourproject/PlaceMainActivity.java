package com.example.tourproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PlaceMainActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    ListView listView;
    EditText edit;
    TextView text;
    ArrayList<Listviewitem> data = new ArrayList<>();
    ListviewAdapter adapter;
    double mapx;
    double mapy;
    String key = "j0aZMFt%2BMMaKgatcd%2F%2FLjwsbfCIfIrLvs6jy9Fyj7EOqvCUnpmXiSbvXlpKbKk2wVC1vlALOF6F1EcG1o1JbzQ%3D%3D";
    TMapGpsManager tmapgps = null;
    TMapPoint tMapPoint = null;
    private boolean m_bTrackingMode = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_main);

        edit= (EditText)findViewById(R.id.edit);
        listView = (ListView)findViewById(R.id.listview);
        /*TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("5e9803e3-81c4-4567-813b-eba6981d2035");*/

        /*tmapgps = new TMapGpsManager(PlaceMainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);

        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 받음. 실내일 때 유용
        tmapgps.setProvider(tmapgps.GPS_PROVIDER);//GPS로 현 위치를 잡음

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        tmapgps.OpenGps();*/
        setGps();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //listView = (ListView)findViewById(R.id.list);

                /*tMapPoint = tmapgps.getLocation();
                mapy = tMapPoint.getLatitude();
                mapx = tMapPoint.getLongitude();*/

                Log.i("관련들어왔어요",Double.toString(mapx));
                Log.i("관련들어왔어요",Double.toString(mapy));
                //tmapgps.CloseGps();
                getXmlData0();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //text.setText(data);

                        adapter=new ListviewAdapter(PlaceMainActivity.this,R.layout.item, data);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();
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
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method....
    public void mOnClick(View v){
        switch( v.getId() ){
            case R.id.button:
                //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                       //listView = (ListView)findViewById(R.id.list);
                        getXmlData1();
                        getXmlData2();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    // TODO Auto-generated method stub
                                //text.setText(data);
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                }).start();
                break;

        }
    }//mOnClick method..

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
    void getXmlData0(){
        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..

        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&mapX="+mapx+"&mapY="+mapy+"&radius=1500";

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
                Log.i("관련들어왔어요","ddddddd");
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
        }
    }//getXmlData method....
    void getXmlData1(){
        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..

        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&contentTypeId=12&keyword="+location;

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
            data.clear();//getXmlData2에는 없음
            xpp.next();
            Listviewitem item1 = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                //Log.i("ddd","ddddddd");
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
        }
    }//getXmlData method....
    void getXmlData2(){
        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..

        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&contentTypeId=14&keyword="+location;

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
            xpp.next();
            Listviewitem item1 = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
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
        }

    }//getXmlData method....

    @Override
    public void onClick(View v) {

    }

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                mapy = location.getLatitude();
                mapx = location.getLongitude();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                            }
                        });

                    }
                }).start();
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

}//MainActivity class..

