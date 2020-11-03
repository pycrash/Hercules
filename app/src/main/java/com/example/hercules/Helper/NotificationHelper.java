package com.example.hercules.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.example.hercules.Constants.Constants;
import com.example.hercules.R;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager manager;
    public static final String TAG = "Notification Helper";

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "NotificationHelper: device android version is greater than Oreo");
            Log.d(TAG, "NotificationHelper: need to create a channel for notification");
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        Log.d(TAG, "createChannel: creating notification channel");
        NotificationChannel channel = new  NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getHerculesChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri) {
        Log.d(TAG, "getHerculesChannelNotification: building notification");
        return new Notification.Builder(getApplicationContext(), Constants.CHANNEL_ID)
                    .setContentIntent(contentIntent)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setSound(soundUri)
                    .setAutoCancel(false);
    }

}
