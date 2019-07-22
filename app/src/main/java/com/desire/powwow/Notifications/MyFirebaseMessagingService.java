package com.desire.powwow.Notifications;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.desire.powwow.Chat.ChatActivity;
import com.desire.powwow.Chat.UserDetails;
import com.desire.powwow.Home;
import com.desire.powwow.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = "MyFirebaseMessagingService";

    public static SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putString("fcmtoken", s);
        Log.d(TAG, " onNewToken " + s);
        editor.commit();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "sendNotification: " + remoteMessage.getData());
        sendNotification(remoteMessage);

//        if (remoteMessage.getNotification() != null) {
//            String jsonstring = remoteMessage.getNotification().getBody();
//            String jsonstring1 = remoteMessage.getNotification().getTitle();
//            Log.d(TAG, "sendNotification: " + jsonstring1);
//
//            try {
//                JSONObject jsonObject = new JSONObject(jsonstring);
//                String title1 = jsonObject.getString("title");
//                String body = jsonObject.getString("body");
//                UserDetails.chatWith = title1;
//                Log.d(TAG, "onMessageReceived: title " + title1 + "::" + body);
//            } catch (Exception e) {
//            }
//            Log.e("FIREBASE", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage);
//        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d(TAG, "sendNotification: title " + notification.getTitle() + " body " + notification.getBody());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_noshadow))
                .setSmallIcon(R.mipmap.logo_noshadow)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}