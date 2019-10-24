package com.example.thief;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;


//시작 화면
public class IntroActivity extends AppCompatActivity {
    String departure="10:00", arrival="11:00", transform="Success";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);


        //Background 작업시 data 전송이 OnMessageRecieve로 안오는 것에 따른 처리
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            // * MapCollection이라는 이상한 놈이 Bundle에 계속 추가되서 예외 처리 **
            if (keys.size() != 1) {
                departure = bundle.get("departure").toString();
                arrival = bundle.get("arrival").toString();
                transform = bundle.get("transform").toString();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    intent.putExtra("departure", departure);
                    intent.putExtra("arrival", arrival);
                    intent.putExtra("transform", transform);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
        }
    }
}
