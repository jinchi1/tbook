package com.travelbooks3.android.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.IntroActivity;
import com.travelbooks3.android.util.LogUtil;

/**
 * Created by system777 on 2017-11-30.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
private static final String TAG = "FirebaseMsgService";

// [START receive_message]
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        LogUtil.d("receive" + remoteMessage.getData());
        sendNotification(remoteMessage.getData().get("message"));
        }

private void sendNotification(String messageBody) {

        LogUtil.d("received message :" + messageBody);

        Intent sendIntent = new Intent("push");
        sendIntent.putExtra("push","push");
        sendBroadcast(sendIntent);
/*
        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,
        PendingIntent.FLAG_ONE_SHOT);*/
/*
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Travelbook")
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0  ID of notification , notificationBuilder.build());*/


}

}
