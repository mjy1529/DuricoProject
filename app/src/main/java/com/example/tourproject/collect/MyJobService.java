package com.example.tourproject.collect;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    JobParameters params;
    DoItTask doIt;
    NotificationCompat.Builder builder;
    static double mapx;
    static double mapy;
    static ArrayList<Listviewitem> data = new ArrayList<>();
    static ArrayList<Listviewitem> data2 = new ArrayList<>();
    static String key = "j0aZMFt%2BMMaKgatcd%2F%2FLjwsbfCIfIrLvs6jy9Fyj7EOqvCUnpmXiSbvXlpKbKk2wVC1vlALOF6F1EcG1o1JbzQ%3D%3D";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.params = jobParameters;
        setGps();
        doIt = new DoItTask();
        doIt.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (doIt != null) {
            doIt.cancel(true);
        }
        return true;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("GPS전 TmapTest", "" + mapx + "," + mapy);

            find(mapx, mapy, 12);
            find(mapx, mapy, 14);
            find2000(mapx, mapy, 12);
            find2000(mapx, mapy, 14);
            Log.d("GPS후 TmapTest", "" + mapx + "," + mapy);
            //조건문 if (푸시알림할 데이터가 있으면) {
            if (data.size() != 0 || data2.size() != 0) {
                //push notification
                Intent intent = new Intent(getApplicationContext(), PlaceMainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel notificationChannel = new NotificationChannel(
                            "smartTour",
                            "smartTour",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(notificationChannel);
                    builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
                } else {
                    builder = new NotificationCompat.Builder(getApplicationContext());
                }

                builder.setSmallIcon(R.drawable.pinicon)
                        .setContentTitle("수집 가능!")
                        .setContentText("근처에 수집 가능한 관광지가 있습니다!!")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


                notificationManager.notify(0, builder.build());
                Log.d("push", "푸시 알림 울림");
            }
            //}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            jobFinished(params, false);
            super.onPostExecute(aVoid);
        }


    }
    static void find(double longi, double lati, int contentTypeNum) {
        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&mapX="+longi+"&mapY="+lati+"&radius=2000&contentTypeId="+contentTypeNum;
        Log.i("함수들어갑니다.","MyJobService find()");
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
    static void find2000(double longi, double lati, int contentTypeNum) {
        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="+key+
                "&MobileOS=ETC&MobileApp=AppTest&mapX="+longi+"&mapY="+lati+"&radius=2000&contentTypeId="+contentTypeNum;
        Log.i("함수들어갑니다.","MyJobService find2000()");
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
    static private Bitmap getImageBitmap(String url) {
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

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("함수들어갑니다.","onLocationChanged");
            if (location != null) {
                mapy = location.getLatitude();
                mapx = location.getLongitude();
                //mapx = 126.9769930325;
                //mapy = 37.5788222356;
                Log.i("잡서미스 리스너들어왔어요", Double.toString(mapx));
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //listView = (ListView)findViewById(R.id.list);
                    Log.i("잡서비스 리스너스레드 들어왔어요",Double.toString(mapx));
                    Log.i("여기나야관련들어왔어요",Double.toString(mapy));
                    find(mapx, mapy, 12);
                    find(mapx, mapy, 14);
                    find2000(mapx, mapy, 12);
                    find2000(mapx, mapy, 14);
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



    public void setGps() {
        Log.i("잡서비스 함수들어갑니다.","setGps");
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

<<<<<<< HEAD
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                0, // 통지사이의 최소 시간간격 (miliSecond)
                0, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                0, // 통지사이의 최소 시간간격 (miliSecond)
=======
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setCostAllowed(false);
//
//        String provider = lm.getBestProvider(criteria, true);

        String provider = LocationManager.PASSIVE_PROVIDER;

//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
//                10000, // 통지사이의 최소 시간간격 (miliSecond)
//                0, // 통지사이의 최소 변경거리 (m)
//                mLocationListener);
        lm.requestLocationUpdates(provider, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                10000, // 통지사이의 최소 시간간격 (miliSecond)
>>>>>>> 9bfbaa310a50273ac5012820592dceb48e4cbea7
                0, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }
}
