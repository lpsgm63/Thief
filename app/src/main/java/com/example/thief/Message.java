package com.example.thief;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//FCM 메시지 사용에 따른 token 생성 및 관리 클래스
public class Message extends FirebaseMessagingService {
    private static final String TAG = "Message" ;

    //구글 token 생성
    //token : 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용
    
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG,"이거 되냐");
        Log.d(TAG,"Refreshed token:" + token);
    }


    //메시지 수신 하지만 Background 작업시 Notification만 동작(데이터 영역은 Intro의 Intent로 전달됨)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG,"FROM: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size()>0){
            Log.d(TAG ,"payload : " + remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body" + remoteMessage.getNotification().getBody());
        }
    }
}

