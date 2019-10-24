/***************************************************************************************************
 *
 *          제목 : 도착시간 알람 및 위치와 시간 그리고 전송 성공 출력 앱
 *
 *          상세 설명 : 라즈베리파이에서 온 데이터를 활용하여 출발과 도착에 대한 시간 및
 *          위치 정보를 각각 시계와 구글 지도로 표시한다. 또한 고객의 만족도에 따라 만족시
 *          감사를 불만족시 고객센터와의 연락을 주선한다.
 *
 **************************************************************************************************/

package com.example.thief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {
    private static final int GRANTED = 101;
    private static final int DENIED = 102;
    private MainHandler dialoghandler = new MainHandler();
    private GoogleMap mMap;
    private String departure="20:32", arrival="21:16", transform="Success";
    private TextView  time_d,time_a,success;
    private int permissionCheck;
    private double longtitude,latitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //휴대폰 gps 현재 위치 정보 반환
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION); //gps사용에 따른 권한 확인
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longtitude = location.getLongitude();
        latitude = location.getLatitude();

        //구글 Map 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //IntroActivity에서 intent를 통해 넘겨준 정보 MainActivity 변수로 변환
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            departure = bundle.get("departure").toString();
            arrival = bundle.get("arrival").toString();
            transform = bundle.get("transform").toString();
        }

        //출발시간 및 종료시간 view 세팅
        time_d = (TextView)findViewById(R.id.departure);
        time_a = (TextView)findViewById(R.id.arrival);
        success = (TextView)findViewById(R.id.transform);
        time_d.setText(departure);
        time_a.setText(arrival);
        success.setText(transform);

        //고객센터 전화 및 만족도 버튼 세팅
        ImageButton call = (ImageButton)findViewById(R.id.sad);
        ImageButton satisfaction = (ImageButton)findViewById(R.id.happy);
        call.setOnClickListener(this);
        satisfaction.setOnClickListener(this);
    }

    //버튼 클릭시 활성화
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sad) {
            PermissionDialog();
        }
        else if(v.getId() == R.id.happy){
            Toast.makeText(MainActivity.this, "만족해주셔서 감사합니다!!!", Toast.LENGTH_SHORT).show();
        }
    }

    //권한 획득 메시지 출력
    private void PermissionDialog(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                dialoghandler.sendEmptyMessage(GRANTED);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                dialoghandler.sendEmptyMessage(DENIED);
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("고객센터로의 전화와 위치를 표시하기위한 권한이 필요해요")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    //Map에서 현재 위치 및 출발 위치(가상)를 Pin으로 출력
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng  JICOBA = new LatLng(37.441423, 126.786858);
        LatLng  Postion = new LatLng(latitude,longtitude);
        MarkerOptions markerOptions1 = new MarkerOptions();
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions1.position(JICOBA);
        markerOptions2.position(Postion);

        markerOptions1.title("지코바치킨");
        markerOptions1.snippet("신천 1동점");
        markerOptions2.title("내 위치");

        mMap.addMarker(markerOptions1);
        mMap.addMarker(markerOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(JICOBA));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    //부하를 덜기 위해 Handler 사용
    public class MainHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GRANTED:
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:01234567890")));
                    break;
                case DENIED:
                    finish();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
