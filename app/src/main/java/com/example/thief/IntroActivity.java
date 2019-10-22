package com.example.thief;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class IntroActivity extends AppCompatActivity {
    String departure="10:00", arrival="11:00", transform="Success";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        Bundle bundle = getIntent().getExtras();
        Set<String> keys = bundle.keySet();
        //MapCollection이라는 이상한 놈이 Bundle에 계속 추가되서 예외 처리
        if(keys.size() != 1){
              departure = bundle.get("departure").toString();
            arrival = bundle.get("arrival").toString();
            transform = bundle.get("transform").toString();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                intent.putExtra("departure",departure);
                intent.putExtra("arrival",arrival);
                intent.putExtra("transform",transform);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
