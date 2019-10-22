package com.example.thief;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Message extends FirebaseMessagingService {
    private static final String TAG = "Message" ;

    @Override
    /*구글 token 생성
    token : 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용
     */

    public void onNewToken(@NonNull String token) {
        Log.d(TAG,"이거 되냐");
        Log.d(TAG,"Refreshed token:" + token);
    }

    /*
    메세지 받을 경우 메시지 구현 부분
     */

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG,"되는지 궁금하다");
        Log.d(TAG,"FROM: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size()>0){
            Log.d(TAG ,"payload : " + remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body" + remoteMessage.getNotification().getBody());
        }
    }
}

