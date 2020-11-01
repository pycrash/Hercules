package com.example.hercules.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.hercules.Helper.NotificationHelper;
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.MyOrders.MyOrders;
import com.example.hercules.R;
import com.example.hercules.Trading.Trading;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService  {
    public final static String TAG = "MyFirebaseMessaging";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



            Log.d(TAG, "onMessageReceived: successfully received notification" );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotificationAPI26(remoteMessage);
        } else {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        Intent intent;
        PendingIntent pendingIntent;
        assert title != null;
        if (title.charAt(0) == 0) {
            intent = new Intent(this, MyOrders.class);
        } else {
            intent = new Intent(this, Trading.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultUriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder = helper.getHerculesChannelNotification(title, message, pendingIntent, defaultUriSound);

        helper.getManager().notify(new Random().nextInt(), builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0, builder.build());
    }
}
