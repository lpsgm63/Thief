package com.example.thief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
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
    String departure="10:00", arrival="11:00", transform="Success";
    private static final int GRANTED = 101;
    private static final int DENIED = 102;
    GoogleMap mMap;

    public MainHandler dialoghandler = new MainHandler();

    TextView  time_d,time_a,success;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
        LineChart lineChart = (LineChart)findViewById(R.id.lineChart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        entries.add(new Entry(16f, 6));
        entries.add(new Entry(5f, 7));
        entries.add(new Entry(3f, 8));
        entries.add(new Entry(7f, 10));
        entries.add(new Entry(9f, 11));

        LineDataSet dataset = new LineDataSet(entries,"#ofCall");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠

        lineChart.setData(data);
        lineChart.animateY(5000);
*/
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            departure = bundle.get("departure").toString();
            arrival = bundle.get("arrival").toString();
            transform = bundle.get("transform").toString();
        }
        time_d = (TextView)findViewById(R.id.departure);
        time_a = (TextView)findViewById(R.id.arrival);
        success = (TextView)findViewById(R.id.transform);
        time_d.setText(departure);
        time_a.setText(arrival);
        success.setText(transform);
        ImageButton call = (ImageButton)findViewById(R.id.sad);
        ImageButton satisfaction = (ImageButton)findViewById(R.id.happy);
        call.setOnClickListener(this);
        satisfaction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sad) {
            PermissionDialog();
        }
        else if(v.getId() == R.id.happy){
            Toast.makeText(MainActivity.this, "만족해주셔서 감사합니다!!!", Toast.LENGTH_SHORT).show();
        }

    }

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
                .setRationaleMessage("고객센터로의 전화를 위한 권한이 필요해요")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(SEOUL);

        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public class MainHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GRANTED:
                    startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:01041921420")));
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
