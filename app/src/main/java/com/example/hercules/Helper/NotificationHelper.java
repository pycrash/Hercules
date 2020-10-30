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

import com.example.hercules.Constants.Constants;
import com.example.hercules.R;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new  NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getHerculesChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri) {
            return new Notification.Builder(getApplicationContext(), Constants.CHANNEL_ID)
                    .setContentIntent(contentIntent)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setSound(soundUri)
                    .setAutoCancel(false);
    }

}
