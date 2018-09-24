package com.example.tourproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class overView extends AppCompatActivity {
    overView overview;
    EditText edit;
    TextView text;
    ImageView imgView;
    TextView titleView;
    TextView addrView;
    TextView overviewView;
    Overviewitem item = null;
    ArrayList<Overviewitem> data=new ArrayList<>();
    String content_id = "";
    Bitmap imagesrc = null;
    String title = "";
    String addr = "";
    String overview2 = "";
    String key="j0aZMFt%2BMMaKgatcd%2F%2FLjwsbfCIfIrLvs6jy9Fyj7EOqvCUnpmXiSbvXlpKbKk2wVC1vlALOF6F1EcG1o1JbzQ%3D%3D";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                imgView = (ImageView)findViewById(R.id.imageview);
                titleView = (TextView)findViewById(R.id.title);
                addrView = (TextView)findViewById(R.id.addr);
                overviewView = (TextView)findViewById(R.id.overview);

                getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        imgView.setImageBitmap(item.getImage());
                        titleView.setText(item.getTitle());
                        addrView.setText(item.getAddr());
                        overviewView.setText(item.getOverview());
                    }
                });
            }
        }).start();
    }
    void getXmlData(){
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
        System.out.println(title + addr + overview2);
        item = new Overviewitem(imagesrc, title, addr, overview2);
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
}
