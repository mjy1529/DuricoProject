package com.example.tourproject.collect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tourproject.R;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class overView2000 extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    overView2000 overview;
    EditText edit;
    TextView text;
    ImageView imgView;
    TextView placeTitleView;
    TextView title1View;
    TextView overviewView;
    TextView title2View;
    TextView useInfoView;
    TextView title3View;
    Overviewitem item = null;
    ArrayList<Overviewitem> data=new ArrayList<>();
    String content_id = "";
    String contentType_id = "";
    Bitmap imagesrc = null;
    String title = "";
    String addr = "";
    String overview2 = "";
    String useInfo = "";
    String key="1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";
    TMapView tMapView = null;
    Context mContext = null;
    String mapx = "";
    String mapy = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view2000);
        mContext = this;
        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");
        contentType_id = intent.getStringExtra("contentType_id");
        mapx = intent.getStringExtra("mapx");
        mapy = intent.getStringExtra("mapy");
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.map_view);

        tMapView = new TMapView(mContext);
        linearLayoutTmap.addView(tMapView);
        tMapView.setSKTMapApiKey("13373001-b7c7-44b2-9036-423b1e4f7441");
        tMapView.setZoomLevel(15);//줌레벨
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        //tMapView.setSightVisible(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                placeTitleView = (TextView)findViewById(R.id.placeTitle);
                imgView = (ImageView)findViewById(R.id.imageview);
                title1View = (TextView)findViewById(R.id.title1);
                overviewView = (TextView)findViewById(R.id.overview);

                title2View = (TextView)findViewById(R.id.title2);
                useInfoView = (TextView)findViewById(R.id.useInfo);

                title3View = (TextView)findViewById(R.id.title3);
                //맵 뷰
                getXmlData1();
                getXmlData2();
                getMap();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        placeTitleView.setText(item.getTitle());
                        imgView.setImageBitmap(item.getImage());
                        overviewView.setText(item.getAddr() + "\n" + item.getOverview());
                        useInfoView.setText(item.getUseInfo());

                    }
                });
            }
        }).start();
    }
    void getMap() {

        Double x = Double.valueOf(mapx);
        Double y = Double.valueOf(mapy);
        Log.i("여기가 바로 좌표다", y.toString());
        TMapPoint tpoint = new TMapPoint(y, x);

        TMapMarkerItem tItem = new TMapMarkerItem();

        tItem.setTMapPoint(tpoint);
        tItem.setVisible(TMapMarkerItem.VISIBLE);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.pinicon);
        tItem.setIcon(bitmap);

        // 핀모양으로 된 마커를 사용할 경우 마커 중심을 하단 핀 끝으로 설정.
        tItem.setPosition(0.5f, 1.0f);         // 마커의 중심점을 하단, 중앙으로 설정
        tMapView.addMarkerItem("tItem", tItem);
        tMapView.setCenterPoint(x, y);
    }
    void getXmlData1(){
        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?serviceKey="+key+
                "&pageSize=10&pageNo=1&startPage=1&MobileOS=ETC&MobileApp=AppTest&contentId=" +content_id +
                "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";

        try {
            System.out.println("파싱시작합니다.");
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                Log.i("ddd","ddddddd");
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작...\n\n");
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
                        else if(tag.equals("overview")){
                            xpp.next();
                            overview2 = xpp.getText();
                            overview2 = overview2.replaceAll("<br />", "");
                            overview2 = overview2.replaceAll("<br>", "");
                        }
                        else if(tag.equals("title")){
                            xpp.next();
                            title = xpp.getText();
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

    }
    void getXmlData2(){
        Log.i("파싱1 끝났다","ddddddd");
        String queryUrl2="http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro?ServiceKey=" +key+
                "&contentTypeId=" +contentType_id +
                "&contentId=" +content_id+
                "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&introYN=Y";
        Log.i("2 들어왔다","ddddddd");
        try {
            System.out.println("파싱시작합니다.");
            URL url= new URL(queryUrl2);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){

                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("infocenter")){
                            xpp.next();
                            useInfo += "<이용문의>\n";
                            useInfo += xpp.getText();
                        }
                        else if(tag.equals("usefee")){
                            xpp.next();
                            useInfo = useInfo.concat("\n<이용요금>\n" + xpp.getText());
                        }
                        else if(tag.contains("usetime")){
                            xpp.next();
                            useInfo = useInfo.concat("\n<이용시간>\n" + xpp.getText());
                            useInfo = useInfo.replaceAll("<br />", "");
                            useInfo = useInfo.replaceAll("<br>", "");
                        }
                        else if(tag.contains("restdate")){
                            xpp.next();
                            useInfo = useInfo.concat("\n<쉬는날>\n" + xpp.getText());
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
        Log.i("끝까지 왔는데","ddddddd");
        System.out.println(useInfo);
        item = new Overviewitem(imagesrc, title, addr, overview2, useInfo);
        //item.setUseInfo(useInfo);
    }
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

    @Override
    public void onLocationChange(Location location) {

    }
}
