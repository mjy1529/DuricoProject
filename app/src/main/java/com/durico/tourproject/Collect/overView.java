package com.durico.tourproject.Collect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.durico.tourproject.MainActivity;
import com.durico.tourproject.StoryList.StoryListActivity;
import com.durico.tourproject.Util.Application;
import com.durico.tourproject.Network.NetworkService;
import com.durico.tourproject.R;
import com.durico.tourproject.Util.UserManager;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class overView extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    overView overview;
    EditText edit;
    TextView text;
    ImageView imgView;
    Button collectBtn;
    TextView placeTitleView;
    TextView title1View;
    TextView overviewView;
    TextView title2View;
    TextView useInfoView;
    TextView title3View;
    com.durico.tourproject.Collect.Overviewitem item = null;
    ArrayList<com.durico.tourproject.Collect.Overviewitem> data = new ArrayList<>();
    String content_id = "";
    String contentType_id = "";
    Bitmap imagesrc = null;
    String title = "";
    String addr = "";
    String overview2 = "";
    String useInfo = "";
    String key = "1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";
    TMapView tMapView = null;
    Context mContext = null;
    String mapx = "";
    String mapy = "";

    String imageSrcUrl = "";
    String insertPlaceCardMessage;

    TextView p_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        //액션바-------------------------------
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        //Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
//        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
//        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
//
//        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
//        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar3, null);
//        actionBar.setCustomView(mCustomView);//\\
//
        Button home = (Button) findViewById(R.id.home);
        p_t = (TextView) findViewById(R.id.p_t);
        //여기까지------------------------------

        mContext = this;
        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");
        contentType_id = intent.getStringExtra("contentType_id");
        mapx = intent.getStringExtra("mapx");
        mapy = intent.getStringExtra("mapy");
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.map_view);

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

                //placeTitleView = (TextView) findViewById(R.id.placeTitle);
                imgView = (ImageView) findViewById(R.id.imageview);
                collectBtn = (Button) findViewById(R.id.collectbtn);
                title1View = (TextView) findViewById(R.id.title1);
                overviewView = (TextView) findViewById(R.id.overview);

                title2View = (TextView) findViewById(R.id.title2);
                useInfoView = (TextView) findViewById(R.id.useInfo);

                title3View = (TextView) findViewById(R.id.title3);
                //맵 뷰
                getXmlData1();
                getXmlData2();
                getMap();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (imagesrc == null)
                            collectBtn.setVisibility(View.GONE);
                        p_t.setText(item.getTitle());
                        //placeTitleView.setText(item.getTitle());
                        imgView.setImageBitmap(item.getImage());
                        overviewView.setText(item.getAddr() + "\n" + item.getOverview());
                        useInfoView.setText(item.getUseInfo());
                    }
                });

                collectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertPlaceCard();
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

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pinicon);
        tItem.setIcon(bitmap);

        // 핀모양으로 된 마커를 사용할 경우 마커 중심을 하단 핀 끝으로 설정.
        tItem.setPosition(0.5f, 1.0f);         // 마커의 중심점을 하단, 중앙으로 설정
        tMapView.addMarkerItem("tItem", tItem);
        tMapView.setCenterPoint(x, y);
    }

    void getXmlData1() {
        String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?serviceKey=" + key +
                "&pageSize=10&pageNo=1&startPage=1&MobileOS=ETC&MobileApp=AppTest&contentId=" + content_id +
                "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";

        try {
            System.out.println("파싱시작합니다.");
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.i("진입:", "getXmlData1");
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("addr1")) {
                            xpp.next();
                            addr = xpp.getText();
                        } else if (tag.equals("firstimage")) {
                            xpp.next();
                            imagesrc = getImageBitmap(xpp.getText());

                            // ********* 추가코드 *******//
                            imageSrcUrl = xpp.getText();
                            // ******************************//
                        } else if (tag.equals("overview")) {
                            xpp.next();
                            overview2 = xpp.getText();
                            overview2 = overview2.replaceAll("<br />", "");
                            overview2 = overview2.replaceAll("<br>", "");
                        } else if (tag.equals("title")) {
                            xpp.next();
                            title = xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) //buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Auto-generated catch blocke.printStackTrace();
        }

    }

    void getXmlData2() {
        Log.i("파싱1 끝났다", "ddddddd");
        String queryUrl2 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro?ServiceKey=" + key +
                "&contentTypeId=" + contentType_id +
                "&contentId=" + content_id +
                "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&introYN=Y";
        Log.i("2 들어왔다", "ddddddd");
        try {
            System.out.println("파싱시작합니다.");
            URL url = new URL(queryUrl2);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("infocenter")) {
                            xpp.next();
                            useInfo += "<이용문의>\n";
                            useInfo += xpp.getText();
                        } else if (tag.equals("usefee")) {
                            xpp.next();
                            useInfo = useInfo.concat("\n<이용요금>\n" + xpp.getText());
                        } else if (tag.contains("usetime")) {
                            xpp.next();
                            useInfo = useInfo.concat("\n<이용시간>\n" + xpp.getText());
                            useInfo = useInfo.replaceAll("<br />", "");
                            useInfo = useInfo.replaceAll("<br>", "");
                        } else if (tag.contains("restdate")) {
                            xpp.next();
                            useInfo = useInfo.concat("\n<쉬는날>\n" + xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) //buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Auto-generated catch blocke.printStackTrace();
        }
        Log.i("끝까지 왔는데", "ddddddd");
        System.out.println(useInfo);
        item = new com.durico.tourproject.Collect.Overviewitem(imagesrc, title, addr, overview2, useInfo);
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

    public void insertPlaceCard() {
        String user_id = UserManager.getInstance().getUserId();
        final String place_card_url = imageSrcUrl;

        NetworkService networkService = Application.getInstance().getNetworkService();
        Call<String> request = networkService.insertPlaceCard(user_id, place_card_url);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    insertPlaceCardMessage = response.body();

                    MyCustomAlertDialog(imageSrcUrl);

                    if (!UserManager.getInstance().getPlaceCardList().contains(place_card_url)) {
                        UserManager.getInstance().getPlaceCardList().add(place_card_url);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void MyCustomAlertDialog(String place_card_url) {
        final Dialog MyDialog = new Dialog(overView.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.pick_customdialong);
        MyDialog.setTitle("My Custom Dialog");
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView cardimage = (ImageView) MyDialog.findViewById(R.id.gacha_card);
        Glide.with(this).load(place_card_url).into(cardimage);

        TextView cardName = (TextView) MyDialog.findViewById(R.id.cardName);
        cardName.setText(item.getTitle());
        TextView cardContent = (TextView) MyDialog.findViewById(R.id.cardContent);

        if(insertPlaceCardMessage.equals("already_exist")) {
            cardContent.setText("이미 수집 완료된 카드입니다!");
        } else {
            cardContent.setText("수집 완료!!");
        }

        LinearLayout card = (LinearLayout) MyDialog.findViewById(R.id.pickview);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
        MyDialog.show();
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
