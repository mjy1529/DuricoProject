package com.durico.tourproject.Collect;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.durico.tourproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    JobParameters params;
    DoItTask doIt;
    NotificationCompat.Builder builder;
    static double mapx;
    static double mapy;
    static ArrayList<Listviewitem> data = new ArrayList<>();
    static ArrayList<Listviewitem> data2 = new ArrayList<>();
    static String key = "1KIDanqdFKdfoDXR8r1aCMlvUc6paBjZnI2nAcjLNSv5E7M8Gidmsy%2F9jtYXRbRsPr8sLoQmb7pOyNZS28Af3Q%3D%3D";
    public static boolean bAppRunned = false;
    public static boolean finished = false;

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 10800000;  // 5초_1초 60000이 1분
    private static final int FASTEST_UPDATE_INTERVAL_MS = 10800000; // 1초_0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    LatLng currentPosition;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.params = jobParameters;
        finished = false;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        onStart();
        //setGps();
        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }
        doIt = new DoItTask();
        doIt.execute();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (doIt != null) {
            bAppRunned = false;
            onStop();
            doIt.cancel(true);
        }
        return true;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    /*ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        /*if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        LatLng DEFAULT_LOCATION = new LatLng(0, 0);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentPosition = new LatLng( location.getLatitude(), location.getLongitude());
        mapy = location.getLatitude();
        mapx = location.getLongitude();
        Log.d(TAG, "onLocationChanged : ");
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());
        Log.d("onLocationChanged : " , markerSnippet);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //listView = (ListView)findViewById(R.id.list);
                Log.i("잡서비스 리스너스레드 mapx",Double.toString(mapx));
                Log.i("잡서비스 리스너스레드 mapy",Double.toString(mapy));
                finished = false;
                find(mapx, mapy, 12);
                find(mapx, mapy, 14);
                find2000(mapx, mapy, 12);
                find2000(mapx, mapy, 14);
            }
        }).start();
        mCurrentLocatiion = location;
    }
    //@Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        //super.onStart();
    }

    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        //super.onStop();
    }
    public String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
            return;
        }
        Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        mRequestingLocationUpdates = true;
    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");

                        if ( mGoogleApiClient.isConnected() == false ) {
                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
    }

    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d("GPS전 TmapTest", "" + mapx + "," + mapy);
            while (mapx == 0 || mapy == 0) ;

            while (data.size() == 0 && data2.size() == 0) ;

            while (data.size() >= data2.size()) ;
            /*try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            Log.d("GPS후 TmapTest", "" + mapx + "," + mapy);

            //조건문 if (푸시알림할 데이터가 있으면) {
            Log.d("현재 접속한 클래스", getTopApplicationClassName(getApplicationContext()));
            if (data.size() > 0 && !bAppRunned) {
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
                /*try {
                    Thread.sleep(1000 * 60 * 15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
            //}
            //mapx = 0; mapy = 0;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            jobFinished(params, false);
            super.onPostExecute(aVoid);
        }


    }

    public static String getTopApplicationClassName(Context context) {
        return ((ActivityManager.RunningTaskInfo) ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0)).topActivity.getClassName();
    }

    static void find(double longi, double lati, int contentTypeNum) {
        String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=" + key +
                "&MobileOS=ETC&MobileApp=AppTest&mapX=" + longi + "&mapY=" + lati + "&radius=300&contentTypeId=" + contentTypeNum;
        Log.i("함수들어갑니다.", "MyJobService find()");
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;
            Bitmap imagesrc = getImageBitmap("https://s3.ap-northeast-2.amazonaws.com/smarttourapp/temp/noimage.png");
            String title = "";
            String content_id = "";
            String addr = "";
            String contentType_id = "";
            String mapx = "";
            String mapy = "";
            if (contentTypeNum == 12)
                data.clear();
            xpp.next();
            Listviewitem item1 = null;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Log.i("관련들어왔어요","ddddddd");
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
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
                        } else if (tag.equals("contentid")) {
                            xpp.next();
                            content_id = xpp.getText();
                        } else if (tag.equals("contenttypeid")) {
                            xpp.next();
                            contentType_id = xpp.getText();
                        } else if (tag.equals("mapx")) {
                            xpp.next();
                            mapx = xpp.getText();
                        } else if (tag.equals("mapy")) {
                            xpp.next();
                            mapy = xpp.getText();
                        } else if (tag.equals("title")) {
                            xpp.next();
                            title = xpp.getText();
                            if (title != null) {
                                item1 = new Listviewitem(imagesrc, title, content_id, addr, contentType_id, mapx, mapy);
                                data.add(item1);
                                imagesrc = getImageBitmap("https://s3.ap-northeast-2.amazonaws.com/smarttourapp/temp/noimage.png");
                            }
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
            Log.i("find1 캐치 에러!", String.valueOf(data.size()));
        }

        Log.i("find 함수 끝냈다", String.valueOf(data.size()));
    }//getXmlData method....

    static void find2000(double longi, double lati, int contentTypeNum) {
        String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=" + key +
                "&MobileOS=ETC&MobileApp=AppTest&mapX=" + longi + "&mapY=" + lati + "&radius=2000&contentTypeId=" + contentTypeNum;
        Log.i("함수들어갑니다.", "MyJobService find2000()");
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); //url위치로 입력스트림 연결 -> 에러에러에러

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;
            Bitmap imagesrc = getImageBitmap("https://s3.ap-northeast-2.amazonaws.com/smarttourapp/temp/noimage.png");
            String title = "";
            String content_id = "";
            String addr = "";
            String contentType_id = "";
            String mapx = "";
            String mapy = "";
            if (contentTypeNum == 12)
                data2.clear();
            xpp.next();
            Listviewitem item1 = null;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Log.i("관련들어왔어요","ddddddd");
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
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
                        } else if (tag.equals("contentid")) {
                            xpp.next();
                            content_id = xpp.getText();
                        } else if (tag.equals("contenttypeid")) {
                            xpp.next();
                            contentType_id = xpp.getText();
                        } else if (tag.equals("mapx")) {
                            xpp.next();
                            mapx = xpp.getText();
                        } else if (tag.equals("mapy")) {
                            xpp.next();
                            mapy = xpp.getText();
                        } else if (tag.equals("title")) {
                            xpp.next();
                            title = xpp.getText();
                            if (title != null) {
                                item1 = new Listviewitem(imagesrc, title, content_id, addr, contentType_id, mapx, mapy);
                                data2.add(item1);
                                imagesrc = getImageBitmap("https://s3.ap-northeast-2.amazonaws.com/smarttourapp/temp/noimage.png");

                            }
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
            Log.i("catch에러!", String.valueOf(data2.size()));
        }
        Log.i("find2 함수 끝냈다", String.valueOf(data2.size()));
        if(contentTypeNum == 14)
            finished = true;
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

    public double getMapx(){
        return mapx;
    }
    public double getMapy(){
        return mapy;
    }


}